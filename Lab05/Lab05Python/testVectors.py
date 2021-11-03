from Crypto.Cipher import AES
from secrets import token_bytes

def encrypt(key, msg, iv):
    cipher = AES.new(key, AES.MODE_CBC, iv)
    return cipher.encrypt(msg)

def decrypt(key, ciphertext, iv):
    cipher = AES.new(key, AES.MODE_CBC, iv)
    return cipher.decrypt(ciphertext)

key = bytes.fromhex('00000000000000000000000000000000')
iv = bytes.fromhex('00000000000000000000000000000000')
msg = bytes.fromhex('f34481ec3cc627bacd5dc3fb08f273e6')

ciphertext = encrypt(key, msg, iv)
plaintext = decrypt(key, ciphertext, iv)

print(f'\nCBCGFSbox128.rsp file')
print(f'Cipher text: {ciphertext.hex()}')
if not plaintext:
    print('Message is corrupted')
else:
    print(f'Plain text: {plaintext.hex()}')

key = bytes.fromhex('000000000000000000000000000000000000000000000000')
iv = bytes.fromhex('00000000000000000000000000000000')
msg = bytes.fromhex('1b077a6af4b7f98229de786d7516b639')

ciphertext = encrypt(key, msg, iv)
plaintext = decrypt(key, ciphertext, iv)

print(f'\nCBCGFSbox192.rsp file')
print(f'Cipher text: {ciphertext.hex()}')
if not plaintext:
    print('Message is corrupted')
else:
    print(f'Plain text: {plaintext.hex()}')

key = bytes.fromhex('0000000000000000000000000000000000000000000000000000000000000000')
iv = bytes.fromhex('00000000000000000000000000000000')
msg = bytes.fromhex('014730f80ac625fe84f026c60bfd547d')

ciphertext = encrypt(key, msg, iv)
plaintext = decrypt(key, ciphertext, iv)

print(f'\nCBCGFSbox256.rsp file')
print(f'Cipher text: {ciphertext.hex()}')
if not plaintext:
    print('Message is corrupted')
else:
    print(f'Plain text: {plaintext.hex()}')