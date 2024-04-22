package com.framework.module.base

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SourceType
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.Instant


@MappedSuperclass
//@SoftDelete(strategy = SoftDeleteType.DELETED, columnName = "deletedOn", converter = DeletedOnConverter::class)
abstract class BaseEntity<T : Serializable> {
    @GeneratedValue
    @Id
    var id: T? = null

    @field:CreationTimestamp(source = SourceType.DB)
    lateinit var createdOn: Instant

    @field:UpdateTimestamp(source = SourceType.DB)
    lateinit var lastUpdatedOn: Instant

//    var deletedOn: Instant? = null

    @Version
    var version: Long = 0

}
//
//@Converter
//class DeletedOnConverter : AttributeConverter<Boolean, Instant?> {
//    override fun convertToDatabaseColumn(boolean: Boolean?) = when (boolean) {
//        true -> Instant.now()
//        else -> null
//    }
//
//    override fun convertToEntityAttribute(instant: Instant?) = instant != null
//
//}