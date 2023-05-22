package com.dev.hirelink.serializer

import com.dev.hirelink.models.IRIConvertible
import com.dev.hirelink.models.JobOffer
import com.google.gson.*
import java.lang.reflect.Type

class ToIriSerializer: JsonSerializer<Any> {
    override fun serialize(
        src: Any?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        if(src is IRIConvertible)
            return JsonPrimitive(src.toIRI())

        throw JsonParseException("Object does not implement ToIri interface. $src")
    }
}