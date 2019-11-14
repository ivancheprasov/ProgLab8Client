package sample.Locales;

import java.util.ListResourceBundle;

public class Nation_pt extends ListResourceBundle {
    public Object[][] getContents(){
        return contents;
    }
    static final Object[][]contents={
            {"welcomeText", "Bem vindo"},
            {"logButton","Entrar"},
            {"regButton","Registrar"},
            {"loginText","Digite o login"},
            {"passwordText","Digite a senha"},
            {"logText","Autorização"},
            {"regText","Registro"},
            {"mailText","Digite seu endereço de email"},
            {"exitButton","Sair"},
            {"importButton","Importar"},
            {"addButton","Adicione"},
            {"add_if_minButton","Adicione menos"},
            {"removeButton","Excluir"},
            {"updateButton","Modificar"},
            {"informationButton","Informação de Recolha"},
            {"type","Digite"},
            {"state","Condição"},
            {"cost1","Custo"},
            {"cost2","Preço"},
            {"date","Date"},
            {"opt","Adic."},
            {"location","Localizaç."},
            {"CakeLayer","Korzh"},
            {"Potato","Batata"},
            {"Soup","Sopa"},
            {"Window","Janela"},
            {"Door","Porta"},
            {"Pants","Calças"},
            {"Socks","Meias"},
            {"name","Nome"},
            {"GOOD","Bom"},
            {"BAD","Ruim"},
            {"Opened","Aberto"},
            {"All","Todo"},
            {"Table","Tabela"},
            {"infoText","Informações de coleta,Tipo de coleta :,Número de elementos na coleção:,Data de inicialização da coleta: "},
            {"addText"," adicionado à coleção."},
            {"importNot","Comando de importação produzido. Nada adicionado à coleção."},
            {"blankName","Digite um nome para adicionar ou remover um objeto."},
            {"modifyName","Você já atribuiu um nome a este item."},
            {"addNotText"," já foi adicionado à coleção. Escolha outro nome."},
            {"removeText"," removido da coleção."},
            {"removeNotText"," não estava na coleção."},
            {"add_if_minNotText","O custo de um item Object não é mínimo, por isso não tem lugar na coleção.."},
            {"foreignText","Você não pode alterar / excluir o Object."},
            {"update"," foi alterado."}
    };
}
