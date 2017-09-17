package com.wft.geodb.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.wft.geodb.client.GeoDbApi;
import com.wft.geodb.client.net.ApiClient;
import com.wft.geodb.client.net.ApiException;
import com.wft.geodb.client.net.GeoDbApiClient;
import com.wft.geodb.client.vo.CitiesResponse;
import com.wft.geodb.client.vo.CitySummary;
import com.wft.geodb.client.vo.CountriesResponse;
import com.wft.geodb.client.vo.CountrySummary;
import com.wft.geodb.client.vo.CurrenciesResponse;
import com.wft.geodb.client.vo.CurrencyDescriptor;
import com.wft.geodb.client.vo.FindCitiesRequest;
import com.wft.geodb.client.vo.FindCountriesRequest;
import com.wft.geodb.client.vo.FindCurrenciesRequest;
import com.wft.geodb.client.vo.FindRegionCitiesRequest;
import com.wft.geodb.client.vo.FindRegionsRequest;
import com.wft.geodb.client.vo.LocationRadiusUnit;
import com.wft.geodb.client.vo.NearLocationRequest;
import com.wft.geodb.client.vo.RegionSummary;
import com.wft.geodb.client.vo.RegionsResponse;

import lombok.extern.slf4j.Slf4j;

@RunWith(JUnit4.class)
@Slf4j
public class GeoDbApiIntegrationTest
{
    private ApiClient apiClient;
    private GeoDbApi api;

    public GeoDbApiIntegrationTest()
    {
        ApiClient client = new GeoDbApiClient();
        client.setApiKey(TestProperties.getApiKey());

        this.apiClient = client;
    }

    @Before
    public void setup()
    {
        this.api = new GeoDbApi(apiClient);
    }

    @Test
    public void testFindCities_namePrefix()
    {
        try
        {
            testFindCities(
                FindCitiesRequest.builder()
                    .namePrefix("Los")
                    .minPopulation(100000)
                    .build());
        } catch (ApiException e)
        {
            handle(e);
        }
    }

    @Test
    public void testFindCities_nearLocation()
    {
        try
        {
            testFindCities(
                FindCitiesRequest.builder()
                    .nearLocation(
                        NearLocationRequest.builder()
                            .latitude(33.831965)
                            .longitude(-118.376601)
                            .radius(100)
                            .radiusUnit(LocationRadiusUnit.MILES)
                            .build())
                    .minPopulation(100000)
                    .build());
        } catch (ApiException e)
        {
            handle(e);
        }
    }

    @Test
    public void testFindCountries_currencyCode()
    {
        try
        {
            testFindCountries(
                FindCountriesRequest.builder()
                    .currencyCode("USD")
                    .build());
        } catch (ApiException e)
        {
            handle(e);
        }
    }

    @Test
    public void testFindCurrencies_countryCode()
    {
        try
        {
            testFindCurrencies(
                FindCurrenciesRequest.builder()
                    .countryCode("US")
                    .build());
        } catch (ApiException e)
        {
            handle(e);
        }
    }

    @Test
    public void testFindRegionCities()
    {
        try
        {
            testFindRegionCities(
                FindRegionCitiesRequest.builder()
                    .countryCode("US")
                    .regionCode("CA")
                    .minPopulation(100000)
                    .build());
        } catch (ApiException e)
        {
            handle(e);
        }
    }

    @Test
    public void testFindRegions()
    {
        try
        {
            testFindRegions(
                FindRegionsRequest.builder()
                    .countryCode("US")
                    .build());
        } catch (ApiException e)
        {
            handle(e);
        }
    }

    private void assertValid(CitiesResponse response)
    {
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNull(response.getErrors());
        assertFalse(response.getData().isEmpty());

        response.getData().forEach(c -> {
            assertValid(c);
        });
    }

    private void assertValid(CitySummary city)
    {
        assertNotNull(city.getId());
        assertTrue(StringUtils.isNotBlank(city.getCity()));
    }

    private void assertValid(CountriesResponse response)
    {
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNull(response.getErrors());
        assertFalse(response.getData().isEmpty());

        response.getData().forEach(c -> {
            assertValid(c);
        });
    }

    private void assertValid(CountrySummary country)
    {
        assertTrue(StringUtils.isNotBlank(country.getCode()));
        assertTrue(StringUtils.isNotBlank(country.getCurrencyCode()));
        assertTrue(StringUtils.isNotBlank(country.getName()));
    }

    private void assertValid(CurrenciesResponse response)
    {
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNull(response.getErrors());
        assertFalse(response.getData().isEmpty());

        response.getData().forEach(c -> {
            assertValid(c);
        });
    }

    private void assertValid(CurrencyDescriptor currency)
    {
        assertTrue(StringUtils.isNotBlank(currency.getCode()));
        assertNotNull(currency.getCountryCodes());
        assertFalse(currency.getCountryCodes().isEmpty());
    }

    private void assertValid(RegionsResponse response)
    {
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNull(response.getErrors());
        assertFalse(response.getData().isEmpty());

        response.getData().forEach(r -> {
            assertValid(r);
        });
    }

    private void assertValid(RegionSummary region)
    {
        assertTrue(StringUtils.isNotBlank(region.getCountryCode()));
        assertTrue(StringUtils.isNotBlank(region.getName()));
    }

    private void handle(ApiException e)
    {
        log.error(e.getResponseBody());
        fail(e.getResponseBody());
    }

    private void log(CitiesResponse response)
    {
        response.getData().forEach(c -> {
            log.info("City: {}", c);
        });

        long totalCount = response.getData().size();

        if (response.getMetadata() != null)
        {
            totalCount = response.getMetadata().getTotalCount();
        }

        log.info("Total resuls: {}", totalCount);
    }

    private void log(CountriesResponse response)
    {
        response.getData().forEach(c -> {
            log.info("Country: {}", c);
        });

        long totalCount = response.getData().size();

        if (response.getMetadata() != null)
        {
            totalCount = response.getMetadata().getTotalCount();
        }

        log.info("Total resuls: {}", totalCount);
    }

    private void log(CurrenciesResponse response)
    {
        response.getData().forEach(c -> {
            log.info("Currency: {}", c);
        });

        long totalCount = response.getData().size();

        if (response.getMetadata() != null)
        {
            totalCount = response.getMetadata().getTotalCount();
        }

        log.info("Total resuls: {}", totalCount);
    }

    private void log(RegionsResponse response)
    {
        response.getData().forEach(c -> {
            log.info("Region: {}", c);
        });

        long totalCount = response.getData().size();

        if (response.getMetadata() != null)
        {
            totalCount = response.getMetadata().getTotalCount();
        }

        log.info("Total resuls: {}", totalCount);
    }

    private void testFindCities(FindCitiesRequest request)
    {
        CitiesResponse response = this.api.findCities(request);

        assertValid(response);

        log(response);
    }

    private void testFindCountries(FindCountriesRequest request)
    {
        CountriesResponse response = this.api.findCountries(request);

        assertValid(response);

        log(response);
    }

    private void testFindCurrencies(FindCurrenciesRequest request)
    {
        CurrenciesResponse response = this.api.findCurrencies(request);

        assertValid(response);

        log(response);
    }

    private void testFindRegionCities(FindRegionCitiesRequest request)
    {
        CitiesResponse response = this.api.findRegionCities(request);

        assertValid(response);

        log(response);
    }

    private void testFindRegions(FindRegionsRequest request)
    {
        RegionsResponse response = this.api.findRegions(request);

        assertValid(response);

        log(response);
    }
}
