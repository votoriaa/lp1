/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

public class funcionarioDao { // Data Acess Object padrão
ConverteDataUtilitario converte = new ConverteDataUtilitario();
 CidadeDao objCidadeDao = new CidadeDao();
 
    public List<Funcionario> getLista() {
        String sql = "select * from funcionario";
        List<Funcionario> lista = new ArrayList<>();
        try {
            PreparedStatement pst = Conexao.getPreparedStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Funcionario objFuncionario = new Funcionario();
                objFuncionario.setCodigo(rs.getInt("codFuncionario"));
                objFuncionario.setNome(rs.getString("nomeFuncionario"));
                objFuncionario.setSalario(rs.getDouble("salarioFuncionario"));   
                objFuncionario.setNascimento(converte.converteCalendario(rs.getDate("nascimentoFuncionario")));
                objFuncionario.setObjCidade(objCidadeDao.localizar(rs.getInt("cidadeFuncionario")));
                lista.add(objFuncionario);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de SQL: " + ex.getMessage());
        }
        return lista;
    }

    public boolean incluir(Cidade objCidade) {
        String sql = "insert into cidade(nomeCidade, ufCidade) values(?,?)";
        try {
            PreparedStatement pst = Conexao.getPreparedStatement(sql);
            pst.setString(1, objCidade.getNome());
            pst.setString(2, objCidade.getUf());
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Cidade cadastrada com sucesso!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cidade não cadastrada!");
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de SQL: " + ex.getMessage());
        }
        return false;
    }

    public boolean alterar(Cidade objCidade) {
        String sql = "update cidade set nomeCidade=?, ufCidade=? where codCidade=?";
        try {
            PreparedStatement pst = Conexao.getPreparedStatement(sql);
            pst.setString(1, objCidade.getNome());
            pst.setString(2, objCidade.getUf());
            pst.setInt(3, objCidade.getCodigo());
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Cidade alterada com sucesso!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cidade não alterada!");
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de SQL: " + ex.getMessage());
        }
        return false;
    }

    public boolean remover(Cidade objCidade) {
        String sql = "delete from cidade where codCidade=?";
        try {
            PreparedStatement pst = Conexao.getPreparedStatement(sql);
            pst.setInt(1, objCidade.getCodigo());
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Cidade excluída com sucesso!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cidade não excluída!");
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de SQL: " + ex.getMessage());
        }
        return false;
    }

    public boolean salvar(Cidade obj) {
        if (obj.getCodigo() == null) {
            return incluir(obj);
        } else {
            return alterar(obj);
        }

    }

}
