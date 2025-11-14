package com.example.classiccarchecklist.data

/**
 * Definition of all checklist items based on the classic car inspection checklist
 * This defines the structure and questions for each section
 */
object ChecklistItemsDefinition {
    
    /**
     * Get all checklist items for a given checklist ID
     * This will be used to initialize a new checklist or load existing items
     */
    fun getAllItems(checklistId: Long): List<ChecklistItemDomain> {
        return buildList {
            // Paint & Body Section
            addAll(getPaintBodyItems(checklistId))
            
            // Engine Bay Section
            addAll(getEngineBayItems(checklistId))
            
            // Lights Section
            addAll(getLightsItems(checklistId))
            
            // Interior Section
            addAll(getInteriorItems(checklistId))
            
            // Windows & Trim Section
            addAll(getWindowsTrimItems(checklistId))
            
            // Undercarriage Section
            addAll(getUndercarriageItems(checklistId))
            
            // Gas Tank Section
            addAll(getGasTankItems(checklistId))
            
            // Trunk Section
            addAll(getTrunkItems(checklistId))
            
            // Tires & Lug Nuts Section
            addAll(getTiresLugNutsItems(checklistId))
            
            // VIN Number & Title Section
            addAll(getVinTitleItems(checklistId))
            
            // Test Drive Section
            addAll(getTestDriveItems(checklistId))
        }
    }
    
    private fun getPaintBodyItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.PAINT_BODY,
                question = "Chips, Scratches, Dings, Dents",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.PAINT_BODY,
                question = "Body lines",
                type = ChecklistItemType.MULTI_CHOICE,
                options = listOf("STRAIGHT", "WAVY", "MISSING")
            ),
            ChecklistItemDomain(
                section = ChecklistSection.PAINT_BODY,
                question = "Door gaps consistent",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.PAINT_BODY,
                question = "Rust",
                type = ChecklistItemType.MULTI_CHOICE,
                options = listOf("SUPERFICIAL", "PITTING", "HOLES", "BLISTERS IN PAINT")
            )
        )
    }
    
    private fun getEngineBayItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Hood opens and closes smoothly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Battery terminals are clean and not corroded",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Belts are not cracked or stringy",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Hoses don't seem too hard, worn or show signs of leakage",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Evidence of fluid leaks",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Where:",
                type = ChecklistItemType.TEXT_INPUT
            ),
            ChecklistItemDomain(
                section = ChecklistSection.ENGINE_BAY,
                question = "Check All Fluid Levels",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getLightsItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.LIGHTS,
                question = "Check all lights are working",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getInteriorItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Any odors, mold or moisture",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Rips, tears, burns stains or holes in upholstery",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Seats feel solid",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Seats slide forward and back, lean forward/back properly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Seatbelts (4) present and fully functional",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Missing knobs or components",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Interior accessories work - heater, lighter, radio, gauges, instruments, etc",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Interior door handles work properly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Convertible top locks down at front windshield properly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Convertible top operates smoothly (power or manual)",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.INTERIOR,
                question = "Top fitment looks acceptable, installed well, not shrunk, weatherstripping in good condition",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getWindowsTrimItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.WINDOWS_TRIM,
                question = "Windows roll up and down smoothly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.WINDOWS_TRIM,
                question = "Trim fits evenly and nicely",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getUndercarriageItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Evidence of leaks",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Worn, cracked, pinched, leaking brake hoses",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Metal lines rusty or pinched",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Hose connection points clean/solid",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Rust or repairs at frame / rocker panel/floor boards (esp under heater core)",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Evidence of worn out suspension or suspension components",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Fuel lines hanging, cracking or leaking",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.UNDERCARRIAGE,
                question = "Evidence of repairs",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getGasTankItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.GAS_TANK,
                question = "Strapped in properly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.GAS_TANK,
                question = "Fuel filler neck and related hoses in good condition",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getTrunkItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.TRUNK,
                question = "Trunk lid opens and closes properly. Stays up/open",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TRUNK,
                question = "Light sockets in good visual, working order",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TRUNK,
                question = "Evidence of water leaking into trunk. Weatherstripping intact",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TRUNK,
                question = "Look under rear window package tray area - Any evidence of repair, water damage, leakage or penetration",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getTiresLugNutsItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.TIRES_LUG_NUTS,
                question = "Tire condition",
                type = ChecklistItemType.MULTI_CHOICE,
                options = listOf("POOR", "FAIR", "GOOD")
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TIRES_LUG_NUTS,
                question = "Check date on tires to make sure they're safe to drive on. Any evidence of shredding, cracking, rubbing, gashing, bulges, lack of tread",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TIRES_LUG_NUTS,
                question = "Lug nuts on all studs",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getVinTitleItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.VIN_TITLE,
                question = "Clear visible VIN tag that does not appear tampered with",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.VIN_TITLE,
                question = "Does VIN number on car match VIN number on title",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.VIN_TITLE,
                question = "Is title in seller's name",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.VIN_TITLE,
                question = "Does seller have registration documents that support title & history",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.VIN_TITLE,
                question = "Does vehicle have a trim tag",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.VIN_TITLE,
                question = "Does the car have the options detailed on trim tag",
                type = ChecklistItemType.YES_NO
            )
        )
    }
    
    private fun getTestDriveItems(checklistId: Long): List<ChecklistItemDomain> {
        return listOf(
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Engine starts easily, idles smoothly and shuts off as it should",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Engine starts when hot and is not sluggish or slow. After test drive, turn it off and wait 10 seconds and restart",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "How is overall feel - drivability",
                type = ChecklistItemType.MULTI_CHOICE,
                options = listOf("POOR", "FAIR", "GOOD", "GREAT")
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does temp gauge work properly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does car operate in 160-210 range",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does temp go up when on the freeway or idling in traffic or at stops",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does oil gauge register at least 20 psi at idle and raise to 30-40+ psi as rev'd to increased rpm",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does car feel solid, loose clunky or rattly",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does transmission shift ok",
                type = ChecklistItemType.YES_NO
            ),
            ChecklistItemDomain(
                section = ChecklistSection.TEST_DRIVE,
                question = "Does car stop ok? Does it shudder, squeal, grind or pull",
                type = ChecklistItemType.YES_NO
            )
        )
    }
}
