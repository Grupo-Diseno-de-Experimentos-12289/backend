package pe.edu.upc.travelmatch.profiles.application.internal.commandservices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.*;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.CartRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartCommandServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ExternalIamService externalIamService;

    @Mock
    private ExternalExperienceService externalExperienceService;

    @InjectMocks
    private CartCommandServiceImpl cartCommandService;

    @Test
    void testHandle_CreateCartSuccessfully() {
        // Arrange
        UserId userId = new UserId(1L);
        CreateCartCommand command = new CreateCartCommand(userId);
        Cart cart = new Cart(userId);

        when(externalIamService.existsUserById(userId)).thenReturn(true);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            return savedCart;
        });

        // Act
        Long resultId = cartCommandService.handle(command);

        // Assert
        verify(externalIamService, times(1)).existsUserById(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testHandle_CreateCart_UserNotFound_ThrowsException() {
        // Arrange
        UserId userId = new UserId(1L);
        CreateCartCommand command = new CreateCartCommand(userId);

        when(externalIamService.existsUserById(userId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartCommandService.handle(command);
        });

        assertTrue(exception.getMessage().contains("not found"));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testHandle_ClearCartSuccessfully() {
        // Arrange
        UserId userId = new UserId(1L);
        ClearCartCommand command = new ClearCartCommand(userId);
        Cart cart = new Cart(userId);
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Optional<Cart> result = cartCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get().getItems().isEmpty());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testHandle_AddCartItemSuccessfully() {
        // Arrange
        UserId userId = new UserId(1L);
        AvailabilityId availabilityId = new AvailabilityId(10L);
        Quantity quantity = new Quantity(2);
        AddCartItemCommand command = new AddCartItemCommand(userId, availabilityId, quantity);
        Cart cart = new Cart(userId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Optional<CartItem> result = cartCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(availabilityId, result.get().getAvailabilityId());
        assertEquals(quantity, result.get().getQuantity());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testHandle_AddCartItem_CartNotFound_ThrowsException() {
        // Arrange
        UserId userId = new UserId(1L);
        AvailabilityId availabilityId = new AvailabilityId(10L);
        Quantity quantity = new Quantity(2);
        AddCartItemCommand command = new AddCartItemCommand(userId, availabilityId, quantity);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            cartCommandService.handle(command);
        });

        assertTrue(exception.getMessage().contains("No cart found"));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testHandle_RemoveCartItemSuccessfully() {
        // Arrange
        UserId userId = new UserId(1L);
        AvailabilityId availabilityId = new AvailabilityId(10L);
        RemoveCartItemCommand command = new RemoveCartItemCommand(userId, availabilityId);
        Cart cart = new Cart(userId);
        pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money price = new pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money(new java.math.BigDecimal(10), "PEN");
        CartItem cartItem = new CartItem(availabilityId, new Quantity(1), price);
        cart.addItem(cartItem);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Optional<Cart> result = cartCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get().getItems().isEmpty());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testHandle_UpdateCartItemQuantitySuccessfully() {
        // Arrange
        UserId userId = new UserId(1L);
        AvailabilityId availabilityId = new AvailabilityId(10L);
        Quantity oldQuantity = new Quantity(1);
        Quantity newQuantity = new Quantity(5);
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(userId, availabilityId, newQuantity);
        Cart cart = new Cart(userId);
        pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money price = new pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money(new java.math.BigDecimal(10), "PEN");
        CartItem cartItem = new CartItem(availabilityId, oldQuantity, price);
        cart.addItem(cartItem);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Optional<CartItem> result = cartCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(newQuantity, result.get().getQuantity());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }
}
