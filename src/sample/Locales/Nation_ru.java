package sample.Locales;

import java.util.ListResourceBundle;

public class Nation_ru extends ListResourceBundle {
    public Object[][] getContents(){
        return contents;
    }
    static final Object[][]contents={
            {"welcomeText","Добро пожаловать"},
            {"logButton","Войти"},
            {"regButton","Зарегистрироваться"},
            {"loginText","Введите логин"},
            {"passwordText","Введите пароль"},
            {"logText","Авторизация"},
            {"regText","Регистрация"},
            {"mailText","Введите адрес электронной почты"},
            {"exitButton","Выйти"},
            {"importButton","Импорт"},
            {"addButton","Добавить"},
            {"add_if_minButton","Добавить меньшее"},
            {"removeButton","Удалить"},
            {"updateButton","Модифицировать"},
            {"informationButton","Информация о коллекции"},
            {"type","Тип"},
            {"state","Состояние"},
            {"cost1","Стоимость"},
            {"cost2","Цена"},
            {"date","Дата"},
            {"opt","Доп."},
            {"location","Локация"},
            {"CakeLayer","Корж"},
            {"Potato","Картошка"},
            {"Soup","Суп"},
            {"Window","Окно"},
            {"Door","Дверь"},
            {"Pants","Штаны"},
            {"Socks","Носки"},
            {"name","Имя"},
            {"GOOD","Хорошее"},
            {"BAD","Плохое"},
            {"Opened","Открытое"},
            {"All","Все"},
            {"Table","Таблица"},
            {"infoText","Информация о коллекции,Тип коллекции: ,Количество элементов в коллекции: ,Дата инициализации коллекции: "},
            {"addText"," добавлен в коллекцию."},
            {"importNot","Команда импорт произведена. Ничего не добавлено в коллекцию."},
            {"blankName","Введите имя для того, чтобы добавить или удалить объект."},
            {"modifyName","Вы уже присвоили этой вещи название."},
            {"addNotText"," уже был добавлен в коллекцию. Выберете другое имя."},
            {"removeText"," убран из коллекции."},
            {"removeNotText"," не находился в коллекции."},
            {"add_if_minNotText","Стоимость вещи Object не минимальна, поэтому ей не место в коллекции."},
            {"foreignText","Здесь ваши полномочия всё, заканчиваются. Вы не можете изменить/удалить Object."},
            {"update"," был изменён."}
    };
}
