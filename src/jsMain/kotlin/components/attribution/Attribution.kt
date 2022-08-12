package components.attribution


import csstype.ClassName
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.p

external interface IAttributionProps : Props {

}

val Attribution = FC<IAttributionProps>("Attribution") { props ->
	val tmdb = a.create {
		className = ClassName("underline")
		href = "https://themoviedb.org/"
		+"TMDB"
	}
	val justWatch = a.create {
		className = ClassName("underline")
		href = "https://www.justwatch.com"
		+"JustWatch"
	}
	p {
		className = ClassName("text-xs text-gray-400")

		+"All movie and provider data is supplied by "
		+tmdb
		+" and "
		+justWatch
	}
}