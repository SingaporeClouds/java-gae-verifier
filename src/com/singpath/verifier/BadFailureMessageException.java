package com.singpath.verifier;

public class BadFailureMessageException extends Exception {

	private String m_msg;

	public BadFailureMessageException(String message) {
		m_msg = message;
	}

	public String toString() {
		return "bad failure message: "+m_msg;
	}
}
