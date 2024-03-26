package io.github.misterseye;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.misterseye.validator.ValidatorExtension;

public class StorageTest {

    @Test
    public void should_test_valid_extension(){
        String fileName="file.jpg";
        boolean result = ValidatorExtension.validateExtension(fileName);
        Assertions.assertTrue(result);
    }
}
