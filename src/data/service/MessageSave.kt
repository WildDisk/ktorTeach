package data.service

import data.entity.Messages
import data.model.UserRegistering
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.data.model.MessageStatus

class MessageSave(
    private val userRegistering: UserRegistering = UserRegistering(),
    private val sendedMessage: String
) : User by userRegistering {
    override fun save() {
        transaction {
            Messages.insert {
                it[message] = sendedMessage
                it[userId] = userRegistering.id
                it[messageStatus] = MessageStatus.OPEN.toString()
            }
        }
    }
}