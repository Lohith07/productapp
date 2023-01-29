package com.template.contracts;

import com.template.states.ProductState;
import org.junit.Test;

public class StateTests {

    //Mock State test check for if the state has correct parameters type
    @Test
    public void hasFieldOfCorrectType() throws NoSuchFieldException {
        ProductState.class.getDeclaredField("msg");
        assert (ProductState.class.getDeclaredField("msg").getType().equals(String.class));
    }
}