

package com.example.vzachos.mapaplication.core.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import com.example.vzachos.mapaplication.core.BootstrapService;
import com.example.vzachos.mapaplication.core.CheckIn;
import com.example.vzachos.mapaplication.core.News;
import com.example.vzachos.mapaplication.core.User;
import com.example.vzachos.mapaplication.core.UserAgentProvider;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests of {@link BootstrapService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BootstrapServiceTest {

    /**
     * Create reader for string
     *
     * @param value
     * @return input stream reader
     * @throws IOException
     */
    private static BufferedReader createReader(String value) throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                value.getBytes(HttpRequest.CHARSET_UTF8))));
    }

    @Mock
    private HttpRequest request;

    private BootstrapService service;

    /**
     * Set up default mocks
     *
     * @throws IOException
     */
    @Before
    public void before() throws IOException {
        service = new BootstrapService("foo", new UserAgentProvider()) {
            protected HttpRequest execute(HttpRequest request) throws IOException {
                return BootstrapServiceTest.this.request;
            }
        };
        doReturn(true).when(request).ok();
    }

    /**
     * Verify getting users with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getUsersEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<User> users = service.getUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    /**
     * Verify getting news with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getContentEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<News> content = service.getNews();
        assertNotNull(content);
        assertTrue(content.isEmpty());
    }

    /**
     * Verify getting checkins with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getReferrersEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<CheckIn> referrers = service.getCheckIns();
        assertNotNull(referrers);
        assertTrue(referrers.isEmpty());
    }
}
