## CLS Login Schema
#### Default endpoint: `http://api.dragonet.org/cls`
Please note that queries are sent using GET method and returned with JSON data. <br />

#### Definitions
Custom usernames are USER_ID and KeyCode with underscore(`_`) in the middle of it. <br />
As for `1234_d6a5f0`, `1234` is USER_ID and `d6a5f0` is the KeyCode. <br />

### Endpoints
#### `/query_token.php`
**Query**: `/query_token.php?username=USER_ID&keycode=KeyCode`<br />
**Warning**: The confusing part in this query is that `username` field is the CLS USER_ID not player's IGN. <br />
**Result**: <br />
```text
{
    "status": "success",        //Success or not
    //Following fields only appears if success
    "ign": "USERNAME",         //The in-game name(username). 
    "client": "CLIENT_TOKEN",  //Client ID
    "token": "ACCESS_TOKEN",   //The access token. 
}
```
**Note**: 
Once you get it, you have to refresh the access token(call to `/refresh` method on MojangAPI) and get a new access token that bind to your server's IP address and upload that token back using `/update_token.php` method on CLS or the player sometimes will expirience `bad login` error. <br />

#### `/update_token.php`
**Query**: `/update_token.php?username=PLAYER_IGN&oldtoken=OLD_TOKEN&newtoken=NEW_TOKEN`<br />
**Warning**: This time `username` field IS the actual player's IGN. <br />
**Result**: <br />
```text
{
    "status": "success",        //Success or not
}
```
**Note**: 
Although it returns something but you can't do anything about it if it fails, it can be server's problem so you may wanna just leave the result alone. 

