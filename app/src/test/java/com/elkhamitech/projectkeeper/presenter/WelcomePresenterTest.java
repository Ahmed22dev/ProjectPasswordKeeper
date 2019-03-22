package com.elkhamitech.projectkeeper.presenter;

import com.elkhamitech.Constants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WelcomePresenterTest {

    private WelcomePresenter welcomePresenter;

    @Mock
    WelcomePresenterListener listener;

    @Before
    public void setUp() throws Exception {

        welcomePresenter = new WelcomePresenter(listener);

    }

    @Test
    public void validateEmptyInput() {
        welcomePresenter.validateInputs("");
        listener.userMessage(Constants.ERROR_EMPTY_TEXT);
    }

    @Test
    public void validatePasswordLengthInput() {
        welcomePresenter.validateInputs("123");
        listener.userMessage(Constants.ERROR_PASSWORD_LENGTH);
    }

    @Test
    public void validateCorrectInputs() {
        welcomePresenter.validateInputs("123456");
        listener.onInputValidationSuccess("123456");
    }


    @Test
    public void validateInputs() {
        validateEmptyInput();
        validatePasswordLengthInput();
        validateCorrectInputs();
    }

    @Test
    public void createPassword() {
    }

    @Test
    public void saveUserPassword() {
        long id = welcomePresenter.saveUserPassword("123456");

        Assert.assertEquals(0,id);
    }

    @Test
    public void saveUserSession() {
    }

    @Test
    public void importDB() {
    }
}