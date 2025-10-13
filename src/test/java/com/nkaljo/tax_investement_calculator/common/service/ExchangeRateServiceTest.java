package com.nkaljo.tax_investement_calculator.common.service;

import com.nkaljo.tax_investement_calculator.common.service.ExchangeRateService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ExchangeRateServiceTest {

    private final ExchangeRateService service = new ExchangeRateService();

    @Test
    void testGetMiddleRateUsd() throws Exception {
        // Fake HTML snippet like the NBS table
        String fakeHtml = """
            <html>
              <body>
                <table>
                  <tr>
                    <td>USD</td><td>1</td><td>1</td><td>1</td><td>110,2749</td>
                  </tr>
                </table>
              </body>
            </html>
            """;
        Document fakeDoc = Jsoup.parse(fakeHtml);

        // Mock Jsoup.connect().get()
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);

            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(fakeDoc);

            // Act
            var rate = service.getRsdExchangeRate("USD", "02.03.2023.");

            // Assert
            assertEquals(110.2749d, rate.get().doubleValue());
        }
    }

    @Test
    void testCurrencyNotFound() throws Exception {
        Document fakeDoc = Jsoup.parse("<html><body><table></table></body></html>");

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);

            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(fakeDoc);

            var rate = service.getRsdExchangeRate("USD", "02.03.2023.");

            assertEquals(Optional.empty(), rate);
        }
    }
}
