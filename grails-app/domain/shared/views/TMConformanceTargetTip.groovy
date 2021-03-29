package shared.views

class TMConformanceTargetTip {

    String conformanceTargetTipIdentifier
    boolean requireFullCompliance

    static belongsTo = [
        provider: TMProvider
    ]

    static constraints = {
        conformanceTargetTipIdentifier nullable: false
    }

    static mapping = {
        table name: 'tm_conformance_target_tips'
        conformanceTargetTipIdentifier column: 'conformance_target_tip_identifier'
        requireFullCompliance column: 'require_compliance'
    }
}
