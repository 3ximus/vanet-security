package entity.vanet;

public enum AttackerEnum {
		NO_ATTACKER,		// attacker no very good no bad person			
    	BAD_POSITIONS, 		// manda posicoes erradas
    	BAD_SIGNATURES, 	// assinatura digital falsa
    	BAD_CERTIFICATE,	// certificado revogado, ou expirado.
    	BAD_TIMESTAMPS,		// mensagens nao frescas (replay attack)
    	BEACON_DOS			// the beacons are multiples and ever lastings
}
