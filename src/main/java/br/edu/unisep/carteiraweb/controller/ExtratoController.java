package br.edu.unisep.carteiraweb.controller;

import br.edu.unisep.carteiraweb.exception.ResourceNotFoundException;
import br.edu.unisep.carteiraweb.helpers.GetUserByToken;
import br.edu.unisep.carteiraweb.model.Extrato;
import br.edu.unisep.carteiraweb.model.Transacao;
import br.edu.unisep.carteiraweb.model.Usuario;
import br.edu.unisep.carteiraweb.repository.ExtratoRepository;
import br.edu.unisep.carteiraweb.repository.TransacaoRepository;
import br.edu.unisep.carteiraweb.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/v1")
public class ExtratoController {

    @Autowired
    private ExtratoRepository extratoRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private GetUserByToken getUserByToken;

    @PostMapping("/extrato/{dataIni}/{dataFim}")
    public ResponseEntity<String> gerarExtrato(@PathVariable String dataIni,
                                               @PathVariable String dataFim)
            throws ParseException, IOException, ResourceNotFoundException {

        //TODO Date Util
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Usuario usuario = getUserByToken.execute();

        Extrato extrato = new Extrato();
        extrato.setUsuario(usuario);
        extrato.setData_inicio(format.parse(dataIni));
        extrato.setData_fim(format.parse(dataFim));
        extrato.setTransacoes(transacaoRepository.findByDataInterval(
                usuario.getId(),
                dataIni,
                dataFim
        ));

        extratoRepository.save(extrato);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, new File(
                        //TODO Achar outra fonte
                        ".src/main/"
                ));
                //TODO Formatação
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                //TODO pegar email
                contentStream.showText("Extrato de " + usuario.getNome());
                contentStream.newLine();
                contentStream.showText("Intervalo de data: " + dataIni + " a " + dataFim);
                contentStream.newLine();
                contentStream.showText("Transações:");
                for (Transacao transacao : extrato.getTransacoes()) {
                    contentStream.newLine();
                    contentStream.showText("Valor: " + transacao.getValor());
                    contentStream.newLine();
                    contentStream.showText("Usuário: " + transacao.getUsuario().getNome());
                    contentStream.newLine();
                    contentStream.showText("Data: " + transacao.getData());
                    contentStream.newLine();
                    contentStream.showText("Descrição: " + transacao.getDescricao());
                }
                contentStream.showText(extrato.getTransacoes().toString());
                contentStream.endText();
            }

            //TODO Função
            String password = usuario.getCpf()
                    .substring(0,
                            Math.min(usuario
                                    .getCpf()
                                    .length(), 5
                            )
                    );

            AccessPermission permission = new AccessPermission();
            StandardProtectionPolicy policy = new StandardProtectionPolicy(
                    password,
                    password,
                    permission
            );

            policy.setEncryptionKeyLength(256);
            policy.setPermissions(permission);

            document.protect(policy);

            document.save(outputStream);
        }

        byte[] pdfBytes = outputStream.toByteArray();

        pdfService.generateAndCache(extrato.getId(), pdfBytes);

        //TODO Esconder o link
        String link = "http://localhost:8080/api/v1/extrato/" + extrato.getId();

        return ResponseEntity.ok(link);
    }

    @GetMapping("/extrato/{id}")
    public ResponseEntity<byte[]> getExtrato(@PathVariable(value = "id") Long extratoId) throws
            ResourceNotFoundException, AccessDeniedException {

        Extrato extrato = extratoRepository.findById(extratoId).orElseThrow(() ->
                new ResourceNotFoundException("Extrato not found for this id :: " + extratoId));

        if (extrato.getDownloads() > 0) {
            extrato.decrementDownloads();
            extratoRepository.save(extrato);

            byte[] pdfBytes = pdfService.get(extratoId);

            if (pdfBytes == null) {
                throw new ResourceNotFoundException("PDF não encontrado :: " + extratoId);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("extrato" +
                            extrato.getUsuario().getNome() +
                            extrato.getData_inicio() +
                            "-" +
                            extrato.getData_fim() +
                            ".pdf"
                    )
                    .build()
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } else {
            throw new AccessDeniedException("Limite de downloads excedido :: " + extratoId);
        }
    }
}
