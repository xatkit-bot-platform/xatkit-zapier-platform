Xatkit Zapier Platform
=====

[![License Badge](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://opensource.org/licenses/EPL-2.0)
[![Wiki Badge](https://img.shields.io/badge/doc-wiki-blue)](https://github.com/xatkit-bot-platform/xatkit/wiki/Xatkit-Zapier-Platform)

⚠ This platform is outdated and not actively supported anymore.

Triggers Zapier worflows (zaps) and receive callbacks from Zapier.

## Providers

The Zapier platform does not define any provider.

## Actions

| Action | Parameters                                                   | Return                         | Return Type | Description                                                 |
| ------ | ------------------------------------------------------------ | ------------------------------ | ----------- | ----------------------------------------------------------- |
| PostAction | - `zapierEndpoint`(**String**): the Zapier endpoint to send the POST request to<br/> - `jsonBody` (**String**): a String representation of the Json object to send in the POST request | `null` | `null` | Send a POST request to the provided `zapierEndpoint` with the provided `jsonBody`. This action doesn't expect any return value, see *PostActionWithCallback* to retrieve value from a zap execution |
| PostActionWithCallback | - `zapierEndpoint`(**String**): the Zapier endpoint to send the POST request to<br/> - `jsonBody` (**String**): a String representation of the Json object to send in the POST request | The retrieved value from the callback payload | String | Send a POST request to the provided `zapierEndpoint` with the provided `jsonBody`. This action awaits for a callback payload with a `value` field containing the value to return |

## Options

The Zapier platform does not support any configuration options.
