package org.springframework.cloud.contract.verifier.messaging.amqp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;

import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Spring rabbit test utility that provides a mock ConnectionFactory to avoid having to connect against a running broker.
 *
 * Set verifier.amqp.mockConnection=true to enable the mocked ConnectionFactory
 *
 * @author Mathias Düsterhöft
 * @since 1.0.2
 */
@Configuration
@ConditionalOnClass({Message.class, RabbitTemplate.class, Mockito.class})
@ConditionalOnMissingClass("org.springframework.integration.core.MessageSource")
@AutoConfigureAfter(ContractVerifierAmqpAutoConfiguration.class)
@ConditionalOnProperty(value = "verifier.amqp.mockConnection", havingValue = "true", matchIfMissing = true)
public class RabbitMockConnectionFactoryAutoConfiguration {

	@Bean
	public ConnectionFactory connectionFactory() {
		com.rabbitmq.client.ConnectionFactory mockConnectionFactory = mock(com.rabbitmq.client.ConnectionFactory.class);
		Connection mockConnection = mock(Connection.class);
		Channel mockChannel = mock(Channel.class);
		try {
			when(mockConnectionFactory.newConnection((ExecutorService) null)).thenReturn(mockConnection);
			when(mockConnection.isOpen()).thenReturn(true);
			when(mockConnection.createChannel()).thenReturn(mockChannel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new CachingConnectionFactory(mockConnectionFactory);
	}
}
