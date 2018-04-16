package bookkeepertests;

import java.util.Date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;
import pl.com.bottega.ecommerce.sharedkernel.*;

/**
 *
 * @author 204641
 */
@RunWith(MockitoJUnitRunner.class)
public class BookKeeperTest {
    
    @Mock
    TaxPolicy taxPolicyMock;
    
    InvoiceRequest invoiceRequest;
    RequestItem requestItemOne, requestItemTwo;
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
        clientData = new ClientData(id, "test-name");
        productData = new ProductData(id, new Money(8), "test-product-data", ProductType.STANDARD, new Date());
        requestItemOne = new RequestItem(productData, 10, new Money(80));
        requestItemTwo = new RequestItem(productData, 10, new Money(80));
        invoiceRequest = new InvoiceRequest(clientData);
        taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(13), "test-tax");
            }
        };
        bookKeeper.issuance(invoiceRequest, taxPolicy);
    }
    
    //State tests
    @Test
    public void issuanceMethodCallWithOnePositionShouldReturnInvoiceWithOnePosition() {
        invoiceRequest.add(requestItemOne);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(1));
    }
    
    @Test
    public void issuanceMethodCallWithNoPositionsShouldReturnInvoiceWithNoPositions() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(0));
    }
    
    //Behavioral tests
    @Captor 
    ArgumentCaptor <ProductType> argumentCaptorProductType;
    @Captor 
    ArgumentCaptor <Money> argumentCaptorMoney;
    
    @Test
    public void issuanceMethodCallWithTwoPositionRequestShouldCallCalculateTaxMethodTwice() {
        invoiceRequest.add(requestItemOne);
        invoiceRequest.add(requestItemTwo);

        when(taxPolicyMock.calculateTax(
                Mockito.any(ProductType.class), Mockito.any(Money.class))).
                thenReturn(new Tax(new Money(5), "test-tax"));

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        verify(taxPolicyMock, times(2)).
                calculateTax(argumentCaptorProductType.capture(), argumentCaptorMoney.capture());
    }
    
    @Test
    public void issuanceMethodCallWithNoPositionsRequestShouldNotCallCalculateTaxMethod() {
        when(taxPolicyMock.calculateTax(
                Mockito.any(ProductType.class), Mockito.any(Money.class))).
                thenReturn(new Tax(new Money(5), "test-tax"));

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);
        verify(taxPolicyMock, times(0)).
                calculateTax(argumentCaptorProductType.capture(), argumentCaptorMoney.capture());
    }
}
