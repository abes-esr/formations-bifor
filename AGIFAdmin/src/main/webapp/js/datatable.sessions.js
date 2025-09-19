/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var asInitVals = new Array();
$(document).ready(function() {
    var oTable = $('#listesessions').dataTable({
        "bSortCellsTop": true,
        "iDisplayLength": 100,
        "aaSorting": [[5, "desc"]],
        "bAutoWidth": false,
        "bStateSave": true,
        "sCookiePrefix": "Liste_Sessions_DataTable_",
        "iCookieDuration": "session",
        "sPaginationType": "full_numbers",
        "aoColumns": [
        {
            "bSortable": true
        },

        {
            "bSortable": true
        },

        {
            "bSortable": true
        },

        {
            "bSortable": true
        },

        {
            "bSortable": true
        },

        {
            "bSortable": false
        },

        {
            "bSortable": false
        },

        {
            "bSortable": false
        },

        {
            "bSortable": false
        }
        ],
        "oLanguage": {
            "sProcessing": "Chargement...",
            "sLengthMenu": "Afficher _MENU_ entrées",
            "sZeroRecords": "Aucun résultat trouvé",
            "sEmptyTable": "Aucune donnée dans la table",
            "sInfo": "Affiche _START_ à _END_ sur un total de _TOTAL_ entrées",
            "sInfoEmpty": "Affiche 0 entrées sur un total de 0 entrées",
            "sInfoFiltered": "(filtrées sur un total de _MAX_ entrées)",
            "sInfoPostFix": "",
            "sSearch": "Chercher sur toutes les colonnes:",
            "sUrl": "",
            "oPaginate": {
                "sFirst": "Première",
                "sPrevious": "Précédente",
                "sNext": "Suivante",
                "sLast": "Dernière"
            },
            "fnInfoCallback": null
        }
    });
   
    $("thead input").keyup(function() {
        /* Filter on the column (the index) of this element */
        //oTable.fnFilter( this.value, $("thead input").index(this) );
        oTable.fnFilter(this.value, $(this).parent().parent().children('th').index($(this).parent()));
    });
    $("thead select").change(function() {
        /* Filter on the column (the index) of this element */
        //if ($(this).prop("selectedIndex")
        oTable.fnFilter(this.value, $(this).parent().parent().children('th').index($(this).parent()));
    });
    /*
     * Support functions to provide a little bit of 'user friendlyness' to the textboxes in
     * the footer
     */
    $("thead input").each(function(i) {
        asInitVals[$(this).parent().parent().children('th').index($(this).parent())] = this.value;
    });
    $("thead input").focus(function() {
        if (this.className == "search_init")
        {
            this.className = "";
            this.value = "";
        }
    });

    $('#effacer_filtres').click(function() {
        var oTable = $('#listesessions').dataTable();
        oTable.fnFilterClear();
        $("thead input").val("");
        $("thead select").val("");
    });

    $("thead input").blur(function(i) {
        if (this.value == "")
        {
            this.className = "search_init";
            this.value = asInitVals[$("thead input").parent().parent().children('th').index($(this).parent())];
        }
    });
    function fill_search_fields() {
        var oSettings = oTable.fnSettings();
        $("thead input").each(function(i) {
            if (oSettings.aoPreSearchCols[$(this).parent().parent().children('th').index($(this).parent())]['sSearch'] != '') {
                $(this).val(oSettings.aoPreSearchCols[$(this).parent().parent().children('th').index($(this).parent())]['sSearch']);
                $(this).removeClass("search_init");
            }
        });
        $("thead select").each(function(i) {
            if (oSettings.aoPreSearchCols[$(this).parent().parent().children('th').index($(this).parent())]['sSearch'] != '') {
                $(this).val(oSettings.aoPreSearchCols[$(this).parent().parent().children('th').index($(this).parent())]['sSearch']);
                $(this).removeClass("search_init");
            }
        });
        if (oSettings.oPreviousSearch['sSearch'] != '') {
            $('.search_field').val(oSettings.oPreviousSearch['sSearch']);
        }
    }
    fill_search_fields();
});