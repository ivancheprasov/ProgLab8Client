package sample.Locales;

import java.util.ListResourceBundle;

public class Nation_es_CR extends ListResourceBundle {
    public Object[][] getContents(){
        return contents;
    }
    static final Object[][]contents={
            {"welcomeText","Bienvenido"},
            {"logButton","Iniciar sesión"},
            {"regButton","Para registrarse"},
            {"loginText","Ingrese login"},
            {"passwordText","Ingrese la contraseña"},
            {"logText","Autorización"},
            {"regText","Registro"},
            {"mailText","Ingrese su dirección de correo"},
            {"exitButton","Salir"},
            {"importButton","Importar"},
            {"addButton","Añadir"},
            {"add_if_minButton","Añadir menos"},
            {"removeButton","Eliminar"},
            {"updateButton","Modificar"},
            {"informationButton","Información de la colección"},
            {"type","Tipo"},
            {"state","Condicion"},
            {"cost1","Costo"},
            {"cost2","Precio"},
            {"date","Fecha"},
            {"opt","Adic."},
            {"location","Ubicacion"},
            {"CakeLayer","Korzh"},
            {"Potato","Papa"},
            {"Soup","Sopa"},
            {"Window","Ventana"},
            {"Door","Puerta"},
            {"Pants","Pantalones"},
            {"Socks","Calcetines"},
            {"name","Nombre"},
            {"GOOD","Bueno"},
            {"BAD","Malo"},
            {"Opened","Abierto"},
            {"All","Todos"},
            {"Table","Mesa"},
            {"infoText","Información de la colección,Tipo de colección :,Número de elementos en la colección :,Fecha de inicialización de la colección: "},
            {"addText"," añadido a la colección."},
            {"importNot","Comando de importación producido. Nada añadido a la colección."},
            {"blankName","Introduzca un nombre para agregar o eliminar un objeto."},
            {"modifyName","Ya has asignado un nombre a este elemento."},
            {"addNotText"," ya se ha añadido a la colección. Elige otro nombre.."},
            {"removeText"," eliminado de la colección."},
            {"removeNotText"," no estaba en la colección."},
            {"add_if_minNotText","El costo de un elemento Object no es mínimo, por lo que no tiene lugar en la colección."},
            {"foreignText","No puedes cambiar / borrar un Object."},
            {"update"," fue cambiado."}
    };
}
