HatRemover
==========

**Requires 1.8+ and ProtocolLib to be installed**

In 1.8 a skin in the tab list will only show a hat layer if the player's entity is being rendered clientside. With
preknowledge of another player's skin this allows someone to know when the other player is in the same chunks as them 
_without_ any client modifications.

This is a plugin that does 1 job: stops all hat layers. Any player connecting to the server will not have their hat 
layer rendered, whether they have it enabled or not and changing the setting in-game will not make it render. All other
 layer options are kept as-is, only the hat is affected. This evens the field and makes hat layer information impossible 
 to abuse.
 
### Installation

Drop the .jar into `/plugins` and reload/restart the server. Any players online when installed will have to relog/change 
their client settings for their hat layer to be removed.

### Configuration

If you want to do more than just force disable the hat layer you can edit the config.yml, here is the default:

```yaml
# Available parts: [CAPE, JACKET, LEFT_SLEEVE, RIGHT_SLEEVE, LEFT_PANT_LEG, RIGHT_PANT_LEG, HAT]
force remove parts: [HAT]
force enable parts: []
```

By adding parts to `force remove parts` and `force enable parts` you can modify which parts are shown.