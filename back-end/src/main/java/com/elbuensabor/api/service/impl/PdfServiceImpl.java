package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.entity.Bill;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.entity.OrderDetail;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.service.PdfService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractPdfView;;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;


@Service
public class PdfServiceImpl extends AbstractPdfView implements PdfService {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Bill bill = (Bill) model.get("bill");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        BaseFont normalFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
        BaseFont boldFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
        Color pink = (new Color(255, 182, 193));
        // document.newPage();
        // Crear footer con múltiples líneas
        Phrase footerPhrase = new Phrase();
        footerPhrase.add(new Chunk("1234-5678" + "\n", new Font(normalFont, 10)));
        footerPhrase.add(new Chunk("BuenSabor@restauranteincreible.com" + "\n", new Font(normalFont, 10)));
        footerPhrase.add(new Chunk("Calle Cualquiera 123, Cualquier Lugar" + "\n", new Font(normalFont, 10)));
        footerPhrase.add(new Chunk("www.buensabor.com", new Font(normalFont, 10)));

        // Configurar footer
        HeaderFooter footer = new HeaderFooter(footerPhrase, false);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);

        HeaderFooter header = new HeaderFooter(
                new Phrase("F  A  C  T  U  R  A", new Font(boldFont,18)), false);
        header.setBorder(Rectangle.NO_BORDER);
        header.setAlignment(Element.ALIGN_RIGHT);
        document.setHeader(header);
        document.open();
      PdfContentByte cb = writer.getDirectContent();
        cb.beginText();
        cb.setFontAndSize(normalFont, 13);
        try {
            if (bill.getOrder().getState() == OrderStatus.CANCELED){
            InputStream imageCancel = getClass().getResourceAsStream("/static/canceled.png");
            if (imageCancel != null) {
                Image cancel = Image.getInstance(ImageIO.read(imageCancel), null);
                cancel.setAbsolutePosition(170, 665);
                cancel.scalePercent(70, 50);
                cancel.setAlignment(Element.ALIGN_CENTER);
                cb.addImage(cancel);
            } else {
                System.out.println("no image Cancel for you");
            }
                cb.setColorFill(new Color(255, 0, 0));
                cb.setTextMatrix(492, 750);
                cb.showText( bill.getLowDate().format(formatter));
            }
            InputStream imageStream = getClass().getResourceAsStream("/static/logo.png");
            if (imageStream != null) {
                Image simple = Image.getInstance(ImageIO.read(imageStream), null);
                simple.setAbsolutePosition(20, 720);
                simple.scalePercent(100,80);
                simple.setAlignment(Element.ALIGN_LEFT);
                document.add(simple);
                document.add(new Paragraph(" "));
               document.add(new Chunk("\n\n\n\n"));
            } else {
                System.out.println("no image Logo for you");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // add text at an absolute position
        cb.setColorFill(new Color(0, 0, 0));
        cb.setTextMatrix(515, 780);
        cb.showText("N°" + String.format("%04d", bill.getId()));
        cb.setTextMatrix(492, 765);
        cb.showText( bill.getGenerationDate().format(formatter));
        cb.endText();


        String address = bill.getOrder().getDeliveryMethod().equals("delivery")
                ? bill.getOrder().getAddress()
                : bill.getOrder().getUser().getAddress();
        String phone = bill.getOrder().getDeliveryMethod().equals("delivery")
                ? bill.getOrder().getPhone()
                : bill.getOrder().getUser().getPhone();
        String apartment = bill.getOrder().getDeliveryMethod().equals("delivery")
                ? bill.getOrder().getApartment()
                : bill.getOrder().getUser().getApartment();
        String paymentType = bill.getOrder().getPaymentType().equals("mp")
                ? "Mercado Pago"
                : "Efectivo";
        // Tabla Usuario
        PdfPTable tableUser = new PdfPTable(2);
        float[] columnWidths = {1.5f, 3f};
        tableUser.setWidths(columnWidths);
        PdfPCell  cell1 = createCell("INFORMACIÓN DE CONTACTO",2,boldFont);
        cell1.setPadding(5);cell1.setBackgroundColor(pink);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableUser.addCell(cell1);
        tableUser.addCell(createCell("Nombre y Apellido" , 1,boldFont));
        tableUser.addCell(createCell(bill.getOrder().getUser().getName() + " " + bill.getOrder().getUser().getLastName(), 1,normalFont));
        tableUser.addCell(createCell("Dirección", 1,boldFont));
        tableUser.addCell(createCell( address, 1,normalFont));
        tableUser.addCell(createCell("Departamento", 1,boldFont));
        tableUser.addCell(createCell( apartment, 1,normalFont));
        tableUser.addCell(createCell("Teléfono", 1,boldFont));
        tableUser.addCell(createCell( phone, 1,normalFont));
        document.add(tableUser);
        document.add(new Paragraph(" "));  // Añadir un párrafo vacío
        // Tabla Pedido
        PdfPTable tableOrder = new PdfPTable(2);
        tableOrder.setWidths(columnWidths);
        PdfPCell  cellTitleOrder = createCell("Pedido N°" + String.format("%04d", bill.getOrder().getId()),2,boldFont);
        cellTitleOrder.setBackgroundColor(pink);
        cellTitleOrder.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOrder.addCell(cellTitleOrder);
        tableOrder.addCell(createCell("Tipo de Pago" , 1,boldFont));
        tableOrder.addCell(createCell(paymentType, 1,normalFont));
        tableOrder.addCell(createCell("Tipo de Retiro", 1,boldFont));
        tableOrder.addCell(createCell( bill.getOrder().getDeliveryMethod(), 1,normalFont));
        document.add(tableOrder);
        // Tabla Detalles Pedido
        PdfPTable tableDetails = new PdfPTable(4);
        PdfPCell  cellTitleDetails = createCell("Detalle de Pedido",4,boldFont);
        cellTitleDetails.setBackgroundColor(pink);
        cellTitleDetails.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableDetails.addCell(cellTitleDetails);
        tableDetails.addCell(createCell("Denominación",2,boldFont));
        tableDetails.addCell(createCell("Cantidad",1,boldFont));
        tableDetails.addCell(createCell("SubTotal",1,boldFont));
        for (OrderDetail details: bill.getOrder().getOrderDetails()) {
            if (details.getProduct() != null){
                tableDetails.addCell(createCell(details.getProduct().getDenomination(),2,normalFont));
            } else{
                tableDetails.addCell(createCell(details.getManufacturedProduct().getDenomination(),2,normalFont));
            }
            tableDetails.addCell(createCell(String.valueOf(details.getQuantity()),1,normalFont));
            tableDetails.addCell(createCell("$"+(String.valueOf(details.getSubtotal())),1,normalFont));
        }
        if (bill.getOrder().getDiscount() != null && bill.getOrder().getDiscount() != 0){
        PdfPCell  cellEmpty2 = createCell("",2,normalFont);
        cellEmpty2.setBorder(Rectangle.NO_BORDER);
        tableDetails.addCell(cellEmpty2);
        PdfPCell  cellDiscount = createCell("DESCUENTO",1,boldFont);
        cellDiscount.setBackgroundColor(pink);
        tableDetails.addCell(cellDiscount);
        tableDetails.addCell(createCell("$"+(String.valueOf(bill.getOrder().getDiscount())),1,normalFont));

        }


        PdfPCell  cellEmpty = createCell("",2,normalFont);
        cellEmpty.setBorder(Rectangle.NO_BORDER);
        tableDetails.addCell(cellEmpty);
        PdfPCell  cellTotal = createCell("TOTAL",1,boldFont);
        cellTotal.setBackgroundColor(pink);
        tableDetails.addCell(cellTotal);
        tableDetails.addCell(createCell("$"+(String.valueOf(bill.getOrder().getTotal())),1,normalFont));
        document.add(tableDetails);
    }


    @Override
    public String createPDF(Bill bill) throws Exception {
        try{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        buildPdfDocument(Map.of("bill", bill), document, writer, null, null);
        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(pdfBytes);

        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }
    private static PdfPCell createCell(String content, int colspan,BaseFont baseFont) {
        PdfPCell cell = new PdfPCell(new Paragraph(content,new Font(baseFont, 12)));
        cell.setColspan(colspan);
        cell.setPadding(5);
        return cell;
    }

}
