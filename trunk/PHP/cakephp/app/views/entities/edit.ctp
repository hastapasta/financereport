
	<script>
	$(function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
		var name = $( "#name" ),
			email = $( "#email" ),
			password = $( "#password" ),
			ratio = $( "#ratio" ),
			datecutoff = $( "#datecutoff" ),
			allFields = $( [] ).add( name ).add( email ).add( password ),
			tips = $( ".validateTips" );

		function updateTips( t ) {
			tips
				.text( t )
				.addClass( "ui-state-highlight" );
			setTimeout(function() {
				tips.removeClass( "ui-state-highlight", 1500 );
			}, 500 );
		}

		function checkLength( o, n, min, max ) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass( "ui-state-error" );
				updateTips( "Length of " + n + " must be between " +
					min + " and " + max + "." );
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp( o, regexp, n ) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass( "ui-state-error" );
				updateTips( n );
				return false;
			} else {
				return true;
			}
		}
		
		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 400,
			width: 350,
			modal: true,
			buttons: {
				"Split": function() {
					var bValid = true;
					allFields.removeClass( "ui-state-error" );

					/*bValid = bValid && checkLength( name, "username", 3, 16 );
					bValid = bValid && checkLength( email, "email", 6, 80 );
					bValid = bValid && checkLength( password, "password", 5, 16 );

					bValid = bValid && checkRegexp( name, /^[a-z]([0-9a-z_])+$/i, "Username may consist of a-z, 0-9, underscores, begin with a letter." );
					// From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
					bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com" );
					bValid = bValid && checkRegexp( password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9" );*/

					bValid = bValid && checkLength(ratio,"ratio",3,10);

					if ( bValid ) {
						$( "#users tbody" ).append( "<tr>" +
							"<td>" + name.val() + "</td>" + 
							"<td>" + email.val() + "</td>" + 
							"<td>" + password.val() + "</td>" +
						"</tr>" ); 
						$( this ).dialog( "close" );
					
						
						tmpurl="<?php echo $this->Html->url(array("controller"=>"entities","action"=>"split"));?>";

						window.location.href= tmpurl + "?entity_id=" + tmp + "&ratio=" + ratio.val() + "&datecutoff=" + datecutoff.val();
						
						
					}
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				allFields.val( "" ).removeClass( "ui-state-error" );
			}
		});

		$( "#split" )
			.button()
			.click(function() {
				tmp = $("#split").attr("name");
				//alert(tmp);
				$( "#dialog-form" ).dialog( "open" );
			});
	});
	</script>
	
	<div id="dialog-form" title="Split Shares">
	<p class="validateTips">Enter the split in the form of #new:#old <BR> (e.g. 2:1 or 1:4)</p><BR>

	<form>
	<fieldset>
		<label for="ratio">Split Ratio</label>
		<input type="text" name="ratio" id="ratio" class="text ui-widget-content ui-corner-all">
		<label for="datecutoff">Date/Time Cutoff</label>
		<input type="text" name="datecutoff" id="datecutoff" class="text ui-widget-content ui-corner-all">	
	</fieldset>
	</form>
	<p>Please be patient; it can take a couple of minutes to effect the changes.</p>
</div>

<div class="entities form">
<?php //echo $this->element('actions'); ?>
<?php echo $this->Form->create('Entity');
for($i = 0; $i < sizeof($this->data['Entity']); $i++){
?>
<fieldset><legend><?php __('Edit Entity'); ?></legend> <?php
echo $this->Form->input('Entity.'.$i.'.id',array('type'=>'hidden'));
echo $this->Form->input('Entity.'.$i.'.ticker');
echo $this->Form->input('Entity.'.$i.'.shares_outstanding');
echo $this->Form->input('Entity.'.$i.'.begin_fiscal_calendar');
echo $this->Form->input('Entity.'.$i.'.last_reported_quarter');
echo $this->Form->input('Entity.'.$i.'.next_report_date');
echo $this->Form->input('Entity.'.$i.'.groups');
echo $this->Form->input('Entity.'.$i.'.actual_fiscal_year_end');
echo $this->Form->input('Entity.'.$i.'.full_name');
echo "<button type=\"button\" id=\"split".$this->data['Entity'][$i]['id']."\" name=\"".$this->data['Entity'][$i]['id']."\">Split</button>";
?>
<script>
	$(function() {
	<?php echo "$( \"#split".$this->data['Entity'][$i]['id']."\" )" ?>
			.button()
			.click(function() {
				<?php echo "tmp = $(\"#split".$this->data['Entity'][$i]['id']."\").attr(\"name\");"; ?>
				//alert(tmp);
				
				$( "#dialog-form" ).dialog( "open" );
			});
	});
</script>
</fieldset>

<?php } 
echo $this->Form->end(__('Submit', true));?>
</div>

