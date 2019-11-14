package sample.Locales;

import java.util.ListResourceBundle;

public class Nation_lt extends ListResourceBundle {
    public Object[][] getContents(){
        return contents;
    }
    static final Object[][]contents={
            {"welcomeText","Laukiame"},
            {"logButton","Prisijungti"},
            {"regButton","Užsiregistruoti"},
            {"loginText","Įveskite prisijungimą"},
            {"passwordText","Įveskite slaptažodį"},
            {"logText","Leidimas"},
            {"regText","Registracija"},
            {"mailText","Įveskite savo el. Pašto adresą"},
            {"exitButton","Atsijungti"},
            {"importButton","Importuoti"},
            {"addButton","Pridėti"},
            {"add_if_minButton","Pridėti mažiau"},
            {"removeButton","Ištrinti"},
            {"updateButton","Keisti"},
            {"informationButton","Rinkimo informacija"},
            {"type","Tipas"},
            {"state","Būklė"},
            {"cost1","Išlaidos"},
            {"cost2","Kaina"},
            {"date","Data"},
            {"opt","Pap."},
            {"location","Vieta"},
            {"CakeLayer","Korzh"},
            {"Potato","Bulvė"},
            {"Soup","Sriuba"},
            {"Window","Langas"},
            {"Door","Durys"},
            {"Pants","Kelnės"},
            {"Socks","Kojinės"},
            {"name","Vardas"},
            {"GOOD","Geras"},
            {"BAD","Blogas"},
            {"Opened","Atidaryti"},
            {"All","Visi"},
            {"Table","Lentelė"},
            {"infoText","Rinkimo informacija,Kolekcijos tipas :,Kolekcijos elementų skaičius :,Kolekcijos inicijavimo data: "},
            {"addText"," pridėta prie kolekcijos."},
            {"importNot","Sukurta importo komanda. Niekas pridėta prie kolekcijos."},
            {"blankName","Įveskite pavadinimą, kurį norite pridėti arba pašalinti objektą."},
            {"modifyName","Jau priskyrėte šį elementą pavadinimui."},
            {"addNotText"," jau buvo pridėta prie kolekcijos. Pasirinkite kitą pavadinimą."},
            {"removeText"," pašalintas iš kolekcijos."},
            {"removeNotText"," nebuvo kolekcijoje."},
            {"add_if_minNotText","Object elemento kaina nėra minimali, todėl rinkinyje nėra vietos."},
            {"foreignText","Negalite keisti / ištrinti Object."},
            {"update"," buvo pakeistas."}
    };
}
