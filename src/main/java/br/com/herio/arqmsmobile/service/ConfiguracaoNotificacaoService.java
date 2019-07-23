package br.com.herio.arqmsmobile.service;

import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacao;
import br.com.herio.arqmsmobile.dominio.ConfiguracaoNotificacaoRepository;
import br.com.herio.arqmsmobile.dominio.Usuario;
import br.com.herio.arqmsmobile.dominio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfiguracaoNotificacaoService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ConfiguracaoNotificacaoRepository configuracaoNotificacaoRepository;

    public ConfiguracaoNotificacao atualizaConfiguracao(Long idUsuario, boolean receberNotificacao) {
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        Optional<ConfiguracaoNotificacao> configOpt = configuracaoNotificacaoRepository.findByUsuarioId(idUsuario);
        ConfiguracaoNotificacao config;
        if (configOpt.isPresent()) {
            config = configOpt.get();
        } else {
            config = new ConfiguracaoNotificacao();
            config.setUsuario(usuario);
        }
        config.setReceberNotificacao(receberNotificacao);
        configuracaoNotificacaoRepository.save(config);
        return config;

    }
}
