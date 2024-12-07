package pt.psoft.g1.psoftg1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.MalformedURLException;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedRecommendationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public FailedRecommendationException(final String string) {
		super(string);
	}

	public FailedRecommendationException(final String string, final MalformedURLException ex) {
		super(string, ex);
	}
}
