package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class StockTest {

    @Test
    @DisplayName("재고의 수량이 주문수량 보다 적은지 확인한다.")
    void isQuantityLessThanTrue() {
        // given
        int orderQuantity = 2;
        Stock stock = Stock.create("001", 1);
        // when
        boolean result = stock.isQuantityLessThan(orderQuantity);
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("재고의 수량이 주문수량 보다 적은지 확인한다.")
    void isQuantityLessThanFalse() {
        // given
        int orderQuantity = 2;
        Stock stock = Stock.create("001", 2);
        // when
        boolean result = stock.isQuantityLessThan(orderQuantity);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주문수량 만큼 재고를 차감할 수 있다.")
    void deductQuantity() {
        // given
        int orderQuantity = 1;
        Stock stock = Stock.create("001", 1);
        // when
        int result = stock.deductQuantity(orderQuantity);
        // then
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("주문수량 보다 재고가 적을 경우 예외가 발생한다.")
    void deductQuantityEx() {
        // given
        int orderQuantity = 2;
        Stock stock = Stock.create("001", 1);

        // when // given
        assertThatThrownBy(() -> stock.deductQuantity(orderQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 재고가 없습니다.");
    }

}