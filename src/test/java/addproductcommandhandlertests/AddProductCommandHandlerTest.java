package addproductcommandhandlertests;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Captor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;


/**
 *
 * @author karko
 */

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {
    
    
    AddProductCommandHandler commandHandler;
    AddProductCommand command;
    Reservation reservation;
    ClientData clientData;
    Product product;
    
    @Mock
    ReservationRepository reservationRepositoryMock;
    @Mock
    ProductRepository productRepositoryMock;
    @Mock
    SuggestionService suggestionServiceMock;
    @Mock
    ClientRepository clientRepositoryMock;
    @Mock
    Reservation reservationMock;

    @Before
    public void setUp(){
        commandHandler = new AddProductCommandHandler();
        clientData = new ClientDataBuilder().setId(Id.generate()).setName("test-client").createClientData();
        command = new AddProductCommand(Id.generate(), Id.generate(), 10);
        
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,
                clientData, new Date());
        
        product = new Product(Id.generate(), new Money(10),
                "test product name", ProductType.FOOD);
        
        when(reservationRepositoryMock.load(any(Id.class)))
                .thenReturn(reservation);        
        
        when(productRepositoryMock.load(any(Id.class)))
                .thenReturn(product);

        Whitebox.setInternalState(commandHandler, "reservationRepository", reservationRepositoryMock);
        Whitebox.setInternalState(commandHandler, "productRepository", productRepositoryMock);
        
        commandHandler.handle(command);
    }
    
    @Captor
    ArgumentCaptor <Id> argumentCaptorId;
    
    @Test
    public void handleMethodCallShouldCallReservationRepositoryLoadOnce() {
        verify(reservationRepositoryMock, times(1)).load(argumentCaptorId.capture());
        assertThat(argumentCaptorId.getValue(), is(command.getOrderId()));
    }
    
    @Test
    public void handleMethodCallShouldCallProductRepositoryLoadOnce() {
        verify(productRepositoryMock, times(1)).load(argumentCaptorId.capture());
        assertThat(argumentCaptorId.getValue(), is(command.getProductId()));
    }
    
    @Captor
    ArgumentCaptor <Reservation> argumentCaptorReservation;
    
    @Test
    public void handleMethodCallShouldCallReservationSaveOnce() {
        verify(reservationRepositoryMock, times(1)).save(argumentCaptorReservation.capture());
        assertThat(argumentCaptorReservation.getValue(), is(reservation));
    }
    
    @Captor
    ArgumentCaptor <Product> argumentCaptorProduct;
    @Captor
    ArgumentCaptor <Integer> argumentCaptorQuantity;
    
    @Test
    public void handleMethodCallShouldCallReservationAddOnce() {
        when(reservationRepositoryMock.load(any(Id.class))).thenReturn(reservationMock);

        commandHandler.handle(command);
        
        verify(reservationMock, times(1)).
                add(argumentCaptorProduct.capture(), argumentCaptorQuantity.capture());
        assertThat(argumentCaptorProduct.getValue(), is(product));
        assertThat(argumentCaptorQuantity.getValue(), is(10));
    }
}