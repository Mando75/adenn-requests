// SVG design taken from https://github.com/n3r4zzurr0/svg-spinners
// LICENSE: MIT

package components.common.spinners


import csstype.ClassName
import kotlinx.js.jso
import react.FC
import react.Props
import react.dom.svg.ReactSVG.svg

external interface BarsScaleMiddleProps : Props {
	var width: Double?
	var height: Double?
	var className: ClassName?
}


val BarsScaleMiddle = FC<BarsScaleMiddleProps>("BarsScaleMiddle") { props ->
	svg {
		width = props.width ?: 24.0
		height = props.height ?: 24.0
		viewBox = "0 0 ${props.width} ${props.height}"
		xmlns = "http://www.w3.org/2000/svg"
		className = props.className

		dangerouslySetInnerHTML = jso {
			__html = svgBody
		}
	}

}

private const val svgBody = """
  <rect x="1" y="6" width="2.8" height="12">
    <animate begin="a.end-0.2s" attributeName="y" calcMode="spline" dur="0.6s" values="6;1;6"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
    <animate begin="a.end-0.2s" attributeName="height" calcMode="spline" dur="0.6s" values="12;22;12"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
  </rect>
  <rect x="5.8" y="6" width="2.8" height="12">
    <animate begin="a.end-0.4s" attributeName="y" calcMode="spline" dur="0.6s" values="6;1;6"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
    <animate begin="a.end-0.4s" attributeName="height" calcMode="spline" dur="0.6s" values="12;22;12"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
  </rect>
  <rect x="10.6" y="6" width="2.8" height="12">
    <animate id="a" begin="0;b.end-0.1s" attributeName="y" calcMode="spline" dur="0.6s" values="6;1;6"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
    <animate begin="0;b.end-0.1s" attributeName="height" calcMode="spline" dur="0.6s" values="12;22;12"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
  </rect>
  <rect x="15.4" y="6" width="2.8" height="12">
    <animate begin="a.end-0.4s" attributeName="y" calcMode="spline" dur="0.6s" values="6;1;6"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
    <animate begin="a.end-0.4s" attributeName="height" calcMode="spline" dur="0.6s" values="12;22;12"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
  </rect>
  <rect x="20.2" y="6" width="2.8" height="12">
    <animate id="b" begin="a.end-0.2s" attributeName="y" calcMode="spline" dur="0.6s" values="6;1;6"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
    <animate begin="a.end-0.2s" attributeName="height" calcMode="spline" dur="0.6s" values="12;22;12"
             keySplines=".14,.73,.34,1;.65,.26,.82,.45" fill="freeze"/>
  </rect>
"""