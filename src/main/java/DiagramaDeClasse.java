import com.tcc.gelato.config.*;
import com.tcc.gelato.controller.*;
import com.tcc.gelato.model.*;
import com.tcc.gelato.model.servidor.M_Navbar;
import com.tcc.gelato.model.servidor.M_Resposta;
import com.tcc.gelato.model.view.*;
import com.tcc.gelato.model.produto.*;
import com.tcc.gelato.repository.*;
import com.tcc.gelato.service.*;
import com.tcc.gelato.GelatoApplication;

@Deprecated
public class DiagramaDeClasse {
    GelatoApplication gelatoApplication;

    C_Cadastro c_cadastro;
    C_Catalogo c_catalogo;
    C_Error c_error;
    C_Inicio c_inicio;
    C_Login c_login;
    C_Pedidos c_pedidos;
    C_Produto c_produto;
    C_Ticket c_ticket;
    C_Transacao c_transacao;

    M_ViewPedido m_viewPedido;
    M_Navbar m_navbar;
    M_Resposta m_resposta;

    M_Ticket m_ticket;
    M_Produto m_produto;
    M_Estoque m_estoque;
    M_Avaliacao m_avaliacao;

    M_AcessoViaUrl m_acessoViaUrl;
    M_Compra m_compra;
    M_DadosResponsavel m_dadosResponsavel;
    M_MetodoPagamento m_metodoPagamento;
    M_Transacao m_transacao;
    M_Usuario m_usuario;

}
