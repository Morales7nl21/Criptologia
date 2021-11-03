from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from keyFiles import saveKey, generateKey
import base64


def encripthText(archivo, keyarc, modo):
    archtex = open(archivo, 'r', encoding="utf-8")
    archllave = open(keyarc, 'rb')
    texto = archtex.read()
    key = base64.b64decode(archllave.read())
    iv = bytes.fromhex('00000000000000000000000000000000')
    
    texbytes = bytes(texto, 'utf-8')
    padded_bytes = pad(texbytes, AES.block_size)

    if modo == "CBC":
        cipher = AES.new(key, AES.MODE_CBC,iv)
    
    else:
       cipher = AES.new(key, AES.MODE_CFB,iv) 

    ciphertext = cipher.encrypt(padded_bytes)
    ciphertext = base64.b64encode(bytes(ciphertext))
    
    nombre = archivo.replace(".txt", ".aes")
    file = open(nombre, 'wb')
    file.write(ciphertext)
    file.close
    print("Archivo cifrado correctamente")
    print("Guardado como: ",nombre)
    print("")


def decripthText(archivo, keyarc, modo):
    archtex = open(archivo, 'rb')
    archllave = open(keyarc, 'rb')
    texto = base64.b64decode(archtex.read())
    key = base64.b64decode(archllave.read())
    iv = bytes.fromhex('00000000000000000000000000000000')
    
    if modo == "CBC":
        cipher = AES.new(key, AES.MODE_CBC, iv)
    
    else:
       cipher = AES.new(key, AES.MODE_CFB, iv) 

    unpade_bytes = cipher.decrypt(texto)
    originaltext = unpad(unpade_bytes, AES.block_size)
    nombre = archivo.replace(".aes", ".dec")
    file = open(nombre, 'w', encoding='utf-8')
    file.write(originaltext.decode('utf-8'))
    file.close
    print("Archivo desencriptado correctamente")
    print("Guardado como: ",nombre)
    print("")
    
#MENU
def menu():

    print("Bienvenido")
    print("Seleccione una opcion")
    print("1. Encriptar MODO CBC")
    print("2. Desencriptar MODO CBC")
    print("3. Encriptat MODO CFB")
    print("4. Desencriptar MODO CFB")
    print("5. Generar llaves")
    print("6. Salir")
    opcion = input("Su opcion:")

    if int(opcion) == 1:
        texto = input("Introduce el nombre del archivo a cifrar:")
        key = input("Introduce el nombre de tu llave:")
        encripthText(texto,key,"CBC")

    elif int(opcion) == 2:
        texto = input("Introduce el nombre del archivo a desencriptar:")
        key = input("Introduce el nombre de tu llave:")
        decripthText(texto,key, "CBC")
    
    elif int(opcion) == 3:
        texto = input("Introduce el nombre del archivo a cifrar:")
        key = input("Introduce el nombre de tu llave:")
        encripthText(texto,key,"CFB")

    elif int(opcion) == 4:
        texto = input("Introduce el nombre del archivo a desencriptar:")
        key = input("Introduce el nombre de tu llave:")
        decripthText(texto,key, "CFB")

    elif int(opcion) == 5:
        saveKey(generateKey(128), '128-bitKey')
        saveKey(generateKey(192), '192-bitKey')
        saveKey(generateKey(256), '256-bitKey')
        print("Se han generado llaves de 128, 192 y 256 bytes")

    return opcion

opcion = 1

while int(opcion) < 6:
    opcion = menu()
