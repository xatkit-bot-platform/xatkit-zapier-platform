package com.xatkit.plugins.zapier.platform.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Headers;
import com.xatkit.core.XatkitException;
import com.xatkit.core.platform.action.RestPostAction;
import com.xatkit.core.session.XatkitSession;
import com.xatkit.plugins.zapier.platform.ZapierPlatform;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Collections;

/**
 * Performs a <i>POST</i> REST request on the Zapier API.
 * <p>
 * This action does not look for a callback value, and assumes the zap has been completed once the Zapier API returns
 * a success code. See {@link PostActionWithCallback} to perform a POST request with callback value.
 */
public class PostAction extends RestPostAction<ZapierPlatform> {

    /**
     * Constructs a {@link PostAction} from the provided {@code runtimePlatform}, {@code xatkitSession}, {@code
     * zapierEndpoint}, and {@code jsonBody}.
     * <p>
     * The provided {@code jsonBody} must be a {@link String} representation of a valid {@link JsonElement}. The
     * provided {@code jsonBody} will be set as the body of the underlying POST request, and can be accessed from Zapier
     * to tune zap execution.
     *
     * @param runtimePlatform the {@link ZapierPlatform} containing this action
     * @param session         the {@link XatkitSession} associated to this action
     * @param zapierEndpoint  the URI of the Zapier endpoint to send the POST request to
     * @param jsonBody        the body of the POST request to send
     * @throws com.google.gson.JsonSyntaxException if the provided {@code jsonBody} is not a valid {@link JsonElement}
     */
    public PostAction(ZapierPlatform runtimePlatform, XatkitSession session, String zapierEndpoint, String jsonBody) {
        super(runtimePlatform, session, Collections.emptyMap(), zapierEndpoint, Collections.emptyMap(),
                new JsonParser().parse(jsonBody));
    }

    /**
     * Handles the response sent by the Zapier API.
     * <p>
     * This method checks that the Zapier API returned a {@code 200} status code with a {@code success} status element
     * in its Json body. If this is not the case a {@link XatkitException} is thrown.
     *
     * @param headers the {@link Headers} of the response
     * @param status  the status code of the response
     * @param body    the body of the response
     * @return {@code null}
     * @throws XatkitException if the response's status code is not {@code 200} or if its content does not contain a
     *                         {@code success} status element
     */
    @Override
    protected Object handleResponse(Headers headers, int status, InputStream body) {
        if (status == 200) {
            JsonElement bodyElement = new JsonParser().parse(new InputStreamReader(body));
            JsonObject bodyObject = bodyElement.getAsJsonObject();
            String bodyStatus = bodyObject.get("status").getAsString();
            if (!bodyStatus.equals("success")) {
                throw new XatkitException(MessageFormat.format("An error occurred when calling the Zapier API " +
                        "(status: {0})", bodyStatus));
            }
        } else {
            throw new XatkitException(MessageFormat.format("Unexpected error from Zapier API (error code {0})",
                    status));
        }
        return null;
    }
}
