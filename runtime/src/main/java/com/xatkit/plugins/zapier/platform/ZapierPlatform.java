package com.xatkit.plugins.zapier.platform;

import com.xatkit.core.XatkitCore;
import com.xatkit.core.platform.RuntimePlatform;
import fr.inria.atlanmod.commons.log.Log;
import org.apache.commons.configuration2.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link RuntimePlatform} that connects and interacts with the Zapier REST API.
 */
public class ZapierPlatform extends RuntimePlatform {

    /**
     * The Json field used to set and retrieve the Xatkit action identifier from a callback payload.
     */
    public static String ACTION_ID_FIELD = "actionId";

    /**
     * The Json field used to retrieve the returned value from a callback payload.
     */
    public static String VALUE_FIELD = "value";

    /**
     * A {@link Map} containing callback values from Zapier.
     * <p>
     * Callback values can be set with <i>Webhooks by Zapier</i> app's <i>POST</i> action, and allow to send values
     * as a result of a zap execution.
     *
     * @see com.xatkit.plugins.zapier.platform.action.PostActionWithCallback
     */
    private Map<String, String> callbackValues = new ConcurrentHashMap<>();

    /**
     * Constructs a {@link ZapierPlatform} with the provided {@code xatkitCore} and {@code configuration}.
     * <p>
     * The {@link ZapierPlatform} registers a new REST endpoint to the {@link com.xatkit.core.server.XatkitServer}
     * with the URI {@code /zapier/callback}, that must be used to return callback values from zap execution.
     * Callback values can be accessed using the
     * {@link com.xatkit.plugins.zapier.platform.action.PostActionWithCallback} action.
     *
     * @param xatkitCore    the {@link XatkitCore} instance managing the platform
     * @param configuration the {@link Configuration} used to initialize the platform
     */
    public ZapierPlatform(XatkitCore xatkitCore, Configuration configuration) {
        super(xatkitCore, configuration);
        this.xatkitCore.getXatkitServer().registerRestEndpoint("/zapier/callback",
                ((headers, params, content) -> {
                    String actionId = content.getAsJsonObject().get(ACTION_ID_FIELD).getAsString();
                    String value = content.getAsJsonObject().get(VALUE_FIELD).getAsString();
                    this.callbackValues.put(actionId, value);
                    return null;
                }));
    }

    /**
     * Returns the callback value for the provided {@code actionId}.
     * <p>
     * If the callback value is not yet set this method will wait for {@code 1000 ms} and try to retrieve it again.
     * If there is no value returned after the 3rd attempts the method returns {@code null} and logs a warning.
     *
     * @param actionId the unique identifier of the action to retrieve the callback value of
     * @return the retrieved value if it exists, {@code null} otherwise
     */
    public String getCallbackValueForAction(String actionId) {
        return getCallbackValueForAction(actionId, 0);
    }

    /**
     * Performs an attempt to retrieve the callback value for the provided {@code actionId}.
     * <p>
     * If the callback value is not yet set this method will wait for {@code 1000 ms} and try to retrieve it again.
     * If there is no value returned after the 3rd attempts the method returns {@code null} and logs a warning.
     *
     * @param actionId the unique identifier of the action the retrieve the callback value of
     * @param attempts the number of attempts already performed
     * @return the retrieved value if it exists, {@code null} otherwise
     */
    private String getCallbackValueForAction(String actionId, int attempts) {
        if (callbackValues.containsKey(actionId)) {
            return callbackValues.remove(actionId);
        } else {
            if (attempts < 3) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.error(e, "An error occurred while waiting for Zapier callback");
                }
                return getCallbackValueForAction(actionId, attempts + 1);
            } else {
                Log.warn("Cannot retrieve the callback value for action {0} after {1} attempts", actionId,
                        attempts + 1);
                return null;
            }
        }
    }

}
