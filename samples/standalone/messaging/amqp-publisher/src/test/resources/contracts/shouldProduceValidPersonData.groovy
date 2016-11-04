package contracts

org.springframework.cloud.contract.spec.Contract.make {
	// Human readable description
	description 'should produce valid person data'
	// Label by means of which the output message can be triggered
	label 'person.created.event'
	// input to the contract
	input {
		// the contract will be triggered by a method
		triggeredBy('createPersonData()')
	}
	// output message of the contract
	outputMessage {
		// destination to which the output message will be sent
		sentTo 'test-exchange'
		headers {
			header('contentType': 'application/json')
		}
		// the body of the output message
		body ([
				id: $(consumer(9), producer(regex("[0-9]+"))),
				name: "Some"
		])
	}
}
