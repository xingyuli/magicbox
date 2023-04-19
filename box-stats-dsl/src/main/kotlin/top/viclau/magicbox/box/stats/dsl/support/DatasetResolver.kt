package top.viclau.magicbox.box.stats.dsl.support

import top.viclau.magicbox.box.stats.dsl.metadata.Dataset
import top.viclau.magicbox.box.stats.dsl.metadata.DatasetIdResolver
import top.viclau.magicbox.box.stats.dsl.metadata.isResolverUndefined
import top.viclau.magicbox.box.stats.dsl.metadata.meetAttributeCombinationRequirements
import top.viclau.magicbox.box.stats.dsl.model.Query
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DatasetIdResolverInstantiationException(message: String) : RuntimeException(message)

class DatasetIdResolutionException(dataset: Dataset) : RuntimeException(dataset.toString())

interface DatasetIdResolverFactory<T : DatasetIdResolver> : (KClass<T>?) -> T

class PrimaryConstructorBasedDatasetIdResolverFactory<T : DatasetIdResolver> : DatasetIdResolverFactory<T> {

    override fun invoke(resolver: KClass<T>?): T {
        if (resolver == null) {
            throw DatasetIdResolverInstantiationException("resolver type is required")
        }

        val primaryConstructor = resolver.primaryConstructor?.takeIf { it.parameters.isEmpty() }
            ?: throw DatasetIdResolverInstantiationException("primary zero-arg constructor is required for $resolver")

        return primaryConstructor.call()
    }

}

object DatasetResolver {

    private val DEFAULT_FACTORY = PrimaryConstructorBasedDatasetIdResolverFactory<DatasetIdResolver>()

    var idResolverFactory: DatasetIdResolverFactory<*> = DEFAULT_FACTORY

    fun resolveId(dataset: KClass<*>, query: Query<*>?): String {
        val datasetAnno = dataset.annotations.filterIsInstance<Dataset>().firstOrNull()
            ?: throw IllegalArgumentException("`dataset` should be annotated with Dataset annotation: $dataset")
        return resolveId(datasetAnno, query)
    }

    private fun resolveId(dataset: Dataset, query: Query<*>?): String {
        val datasetId: String? = dataset.id.takeIf { it.isNotEmpty() } ?: run {
            val resolverType = if (dataset.isResolverUndefined()) null else dataset.resolver
            val resolverInstance = getIdResolver(resolverType as? KClass<DatasetIdResolver>)
            resolverInstance.resolve(dataset.name, query)
        }
        return datasetId ?: throw DatasetIdResolutionException(dataset)
    }

    private fun <T : DatasetIdResolver> getIdResolver(resolver: KClass<T>?): T {
        return (idResolverFactory as DatasetIdResolverFactory<T>).invoke(resolver)
    }

    fun meetAttributeCombinationRequirements(dataset: Dataset): Boolean {
        val (ok, requiresCustomFactory) = dataset.meetAttributeCombinationRequirements()
        return ok && (!requiresCustomFactory || idResolverFactory != DEFAULT_FACTORY)
    }

}
