package com.xatkit.plugins.zapier.platform.action;

import com.mashape.unirest.http.Headers;
import com.xatkit.core.session.XatkitSession;
import com.xatkit.plugins.zapier.platform.ZapierPlatform;

import java.io.InputStream;
import java.util.UUID;

/**
 * Performs a <i>POST</i> REST request on the Zapier API and waits for a callback value.
 * <p>
 * This action sets a {@link ZapierPlatform#ACTION_ID_FIELD} in the POST request that is used to uniquely identify
 * the action. This field <b>must be set in the callback payload</b> to enable its retrieval.
 * <p>
 * The callback payload must also contain a {@link ZapierPlatform#VALUE_FIELD} that contains the return value of the
 * zap execution. This field is extracted by Xatkit and returned as the result of this action.
 * <p>
 * The callback URL is specified in Zapier's <i>Webhooks by Zapier</i> app's POST action, and should point to {@code
 * <xatkit_url>/zapier/callback}.
 */
public class PostActionWithCallback extends PostAction {

    /**
     * The unique identifier of the Xatkit action.
     * <p>
     * The {@code actionId} is used to retrieve the callback values from the triggered zap. It is set in the sent
     * payload using the {@link ZapierPlatform#ACTION_ID_FIELD} field.
     *
     * @see ZapierPlatform#getCallbackValueForAction(String)
     */
    protected String actionId;

    /**
     * Constructs a {@link PostActionWithCallback} from the provided {@code runtimePlatform}, {@code xatkitSession},
     * {@code zapierEndpoint}, and {@code jsonBody}.
     * <p>
     * The provided {@code jsonBody} must be a {@link String} representation of a valid
     * {@link com.google.gson.JsonElement}. This constructor appends a {@link ZapierPlatform#ACTION_ID_FIELD} field
     * to the provided {@code jsonBody} containing this action's unique identifier, and will set it as the body of
     * the underlying POST request.
     *
     * @param runtimePlatform the {@link ZapierPlatform} containing this action
     * @param session         the {@link XatkitSession} associated to this action
     * @param zapierEndpoint  the URI of the Zapier endpoint to send the POST request to
     * @param jsonBody        the body of the POST request to send
     * @throws com.google.gson.JsonSyntaxException if the provided {@code jsonBody} is not a valid
     *                                             {@link com.google.gson.JsonElement}.
     */
    public PostActionWithCallback(ZapierPlatform runtimePlatform, XatkitSession session, String zapierEndpoint,
                                  String jsonBody) {
        super(runtimePlatform, session, zapierEndpoint, jsonBody);
        this.actionId = UUID.randomUUID().toString();
        this.jsonContent.getAsJsonObject().addProperty("actionId", this.actionId);
    }

    /**
     * Handles the response sent by the Zapier API.
     * <p>
     * This method relies on {@link PostAction}'s super implementation to handle the raw response from the Zapier API
     * . If the received response is a success, this method will wait to receive the callback value using
     * {@link ZapierPlatform#getCallbackValueForAction(String)}.
     * <p>
     * <b>Note</b>: the callback payload must contain a {@link ZapierPlatform#ACTION_ID_FIELD} with the {@code actionId}
     * set in its triggering payload, otherwise the callback will not be associated to this action by the
     * {@link ZapierPlatform}.
     *
     * @param headers the {@link Headers} of the response
     * @param status  the status code of the response
     * @param body    the body of the response
     * @return the value stored in the {@link ZapierPlatform#VALUE_FIELD} of the callback payload
     */
    @Override
    protected Object handleResponse(Headers headers, int status, InputStream body) {
        /*
         * handleResponse for regular post actions do not return anything.
         */
        super.handleResponse(headers, status, body);
        /*
         * Don't use body after the call to super: it has been exhausted to build and parse the Json object and
         * cannot be read again.
         */
        return this.runtimePlatform.getCallbackValueForAction(actionId);
    }
}
