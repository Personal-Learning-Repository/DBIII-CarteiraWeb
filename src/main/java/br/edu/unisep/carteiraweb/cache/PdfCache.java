package br.edu.unisep.carteiraweb.cache;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class PdfCache {
    private final Map<Long, byte[]> pdfCache = new ConcurrentHashMap<>();

    public void put(Long id, byte[] pdf) {
        pdfCache.put(id, pdf);
    }

    public byte[] get(Long id) {
        return pdfCache.get(id);
    }

}
