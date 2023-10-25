package br.edu.unisep.carteiraweb.service;

import br.edu.unisep.carteiraweb.cache.PdfCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final PdfCache pdfCache;

    public void generateAndCache(Long id, byte[] pdf) {
        pdfCache.put(id, pdf);
    }

    public byte[] get(Long id) {
        return pdfCache.get(id);
    }
}
