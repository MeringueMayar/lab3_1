/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookkeepertests;

import java.util.Date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.*;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;
import pl.com.bottega.ecommerce.sharedkernel.*;

/**
 *
 * @author karko
 */
public class BookKeeperTest {
    
    InvoiceRequest invoiceRequest;
    RequestItem requestItem;
    ProductData productData;
    ClientData clientData;
    BookKeeper bookKeeper;
    InvoiceFactory invoiceFactory;
    TaxPolicy taxPolicy;
    @Before
    public void setUp(){
        invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
        Id id = Id.generate();
        clientData = new ClientData(id, "testname");
        productData = new ProductData(id, new Money(8), "tes-product-data", ProductType.STANDARD, new Date());
        requestItem = new RequestItem(productData, 10, new Money(80));
        invoiceRequest = new InvoiceRequest(clientData);
        taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(13), "testtax");
            }
        };
        bookKeeper.issuance(invoiceRequest, taxPolicy);
    }
    
    @Test
    public void issuanceMethodCallWithOnePositionShouldReturnInvoiceWithOnePosition() {
        invoiceRequest.add(requestItem);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(1));
    }
}
