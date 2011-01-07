<html>
<head>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="animatedcollapse.js">

/***********************************************
* Animated Collapsible DIV v2.4- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
***********************************************/

</script>


<script type="text/javascript">

animatedcollapse.addDiv('jason', 'fade=1,height=80px')
animatedcollapse.addDiv('kelly', 'fade=1,height=100px')
animatedcollapse.addDiv('michael', 'fade=1,height=120px')

animatedcollapse.addDiv('cat', 'fade=0,speed=400,group=pets')
animatedcollapse.addDiv('dog', 'fade=0,speed=400,group=pets,persist=1,hide=1')
animatedcollapse.addDiv('rabbit', 'fade=0,speed=400,group=pets,hide=1')

animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
	//$: Access to jQuery
	//divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
	//state: "block" or "none", depending on state
}

animatedcollapse.init()

</script>
</head>
<body>
<b><a href="javascript:animatedcollapse.show(['jason', 'kelly', 'michael'])">Show Examples 1, 2, 3</a> | <a href="javascript:animatedcollapse.hide(['jason', 'kelly', 'michael'])">Hide Examples 1, 2, 3</a></b>

<p><b>Example 1 (individual):</b></p>

<a href="javascript:animatedcollapse.toggle('jason')"><img src="toggle.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('jason')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('jason')">Slide Up</a>

<div id="jason" style="width: 300px; background: #FFFFCC; display:none">
<b>Content inside DIV!</b><br />
<b>Note: Fade effect enabled. Height programmically defined. DIV hidden using inline CSS.</b><br />
</div>


<p><b>Example 2 (individual):</b></p>

<a href="javascript:animatedcollapse.toggle('kelly')"><img src="toggle.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('kelly')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('kelly')">Slide Up</a>

<div id="kelly" style="width: 300px; background: #D2FBFF; display:none">
<b>Content inside DIV!</b><br />
<b>Note: Fade effect enabled. Height programmically defined. DIV hidden using inline CSS.</b><br />
</div>



<p><b>Example 3 (individual):</b></p>

<a href="javascript:animatedcollapse.toggle('michael')"><img src="toggle.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('michael')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('michael')">Slide Up</a>

<div id="michael" style="width: 300px; background: #E7FFCC; display:none">
<b>Content inside DIV!</b><br />
<b>Note: Fade effect enabled. Height programmically defined. DIV hidden using inline CSS.</b><br />
</div>


<hr style="margin: 1em 0" />



<p><b>Example 4 (part of group "pets"):</b></p>

<a href="#" rel="toggle[cat]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('cat')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('cat')">Slide Up</a>

<div id="cat" style="width: 400px; background: #BDF381;">
The cat (Felis catus), also known as the domestic cat or house cat to distinguish it from other felines, is a small carnivorous species of crepuscular mammal that is often valued by humans for its companionship and its ability to hunt vermin. It has been associated with humans for at least 9,500 years. A skilled predator, the cat is known to hunt over 1,000 species for food. It can be trained to obey simple commands.
</div>



<p><b>Example 5 (part of group "pets"):</b></p>

<a href="#" rel="toggle[dog]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('dog')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('dog')">Slide Up</a>

<div id="dog" style="width: 400px; background: #BDF381;">
The dog (Canis lupus familiaris) is a domesticated subspecies of the wolf, a mammal of the Canidae family of the order Carnivora. The term encompasses both feral and pet varieties and is also sometimes used to describe wild canids of other subspecies or species. The domestic dog has been one of the most widely kept working and companion animals in human history, as well as being a food source in some cultures.
</div>



<p><b>Example 6 (part of group "pets"):</b></p>

<a href="#" rel="toggle[rabbit]" data-openimage="collapse.jpg" data-closedimage="expand.jpg"><img src="collapse.jpg" border="0" /></a> <a href="javascript:animatedcollapse.show('rabbit')">Slide Down</a> || <a href="javascript:animatedcollapse.hide('rabbit')">Slide Up</a>

<div id="rabbit" style="width: 400px; background: #BDF381">
Rabbits are ground dwellers that live in environments ranging from desert to tropical forest and wetland. Their natural geographic range encompasses the middle latitudes of the Western Hemisphere. In the Eastern Hemisphere rabbits are found in Europe, portions of Central and Southern Africa, the Indian subcontinent, Sumatra, and Japan.
</div>
</body>
</html>