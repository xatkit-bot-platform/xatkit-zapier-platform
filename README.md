Xatkit Zapier Platform
=====

[![License Badge](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://opensource.org/licenses/EPL-2.0)
[![Build Status](https://travis-ci.com/xatkit-bot-platform/xatkit-zapier-platform.svg?branch=master)](https://travis-ci.com/xatkit-bot-platform/xatkit-zapier-platform)
[![Wiki Badge](https://img.shields.io/badge/doc-wiki-blue)](https://github.com/xatkit-bot-platform/xatkit-releases/wiki/Xatkit-Zapier-Platform)

Triggers Zapier worflows (zaps) and receive callbacks from Zapier. This platform is bundled with the [Xatkit release](https://github.com/xatkit-bot-platform/xatkit-releases/releases).

## Providers

The Zapier platform does not define any provider.

## Actions

| Action | Parameters                                                   | Return                         | Return Type | Description                                                 |
| ------ | ------------------------------------------------------------ | ------------------------------ | ----------- | ----------------------------------------------------------- |
| PostAction | - `zapierEndpoint`(**String**): the Zapier endpoint to send the POST request to<br/> - `jsonBody` (**String**): a String representation of the Json object to send in the POST request | `null` | `null` | Send a POST request to the provided `zapierEndpoint` with the provided `jsonBody`. This action doesn't expect any return value, see *PostActionWithCallback* to retrieve value from a zap execution |
| PostActionWithCallback | - `zapierEndpoint`(**String**): the Zapier endpoint to send the POST request to<br/> - `jsonBody` (**String**): a String representation of the Json object to send in the POST request | The retrieved value from the callback payload | String | Send a POST request to the provided `zapierEndpoint` with the provided `jsonBody`. This action awaits for a callback payload with a `value` field containing the value to return |

## Options

The Zapier platform does not support any configuration options.
