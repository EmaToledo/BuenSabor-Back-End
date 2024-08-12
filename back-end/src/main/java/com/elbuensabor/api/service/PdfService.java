package com.elbuensabor.api.service;

import com.elbuensabor.api.entity.Bill;

public interface PdfService {
    String createPDF(Bill bill) throws Exception;

}
