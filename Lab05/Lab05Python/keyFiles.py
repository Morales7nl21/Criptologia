from Crypto.Cipher import AES
from secrets import token_bytes
import base64

def generateKey(length):
    if(length == 128):
        return token_bytes(16)
    elif(length == 192):
        return token_bytes(24)
    elif(length == 256):
        return token_bytes(32)

def saveKey(key, filename):
    keyFile = open(filename + '.txt', 'wb')
    keyFile.write(base64.b64encode(key))
    keyFile.close()

def recoverKey(filename):
    keyFile = open(filename + '.txt', 'rb')
    key = base64.b64decode(keyFile.read())
    keyFile.close()
    return key

saveKey(generateKey(128), '128-bitKey')
saveKey(generateKey(192), '192-bitKey')
saveKey(generateKey(256), '256-bitKey')