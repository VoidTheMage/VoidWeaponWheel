SCRIPT_START
{
NOP

SCRIPT_NAME VWWMAIN

LVAR_INT selectedUISlot detonSelected slowMotion isP2Instance scrollSelect

LVAR_INT scplayer weaponSlot weaponID currentCPAD iTemp0 iTemp1 iTemp2 deselectDelayKB deselectDelaySCR deselectDelayJP wasWheelOpened wheelDelayed child p2ScriptAddress ammo settings modsFound vwwASI

LVAR_FLOAT fTemp0 fTemp1 fTemp2 x y cursorX cursorY angle distance

IF NOT LOAD_DYNAMIC_LIBRARY "VoidWeaponWheel.asi" vwwASI
    TERMINATE_THIS_CUSTOM_SCRIPT
ENDIF

GET_LABEL_POINTER Settings settings
GET_LABEL_POINTER ModsFound modsFound
WRITE_STRUCT_OFFSET settings 124 4 modsFound

IF IS_ON_SAMP
    READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DisableOnSAMP" iTemp0
    IF iTemp0 = TRUE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF

    WRITE_STRUCT_OFFSET modsFound 0 4 1
ENDIF

IF GET_LOADED_LIBRARY "KeepNoAmmo.SA.asi" iTemp0
    WRITE_STRUCT_OFFSET modsFound 4 4 1
ENDIF

IF LOAD_DYNAMIC_LIBRARY "GInputSA.asi" iTemp0
    WRITE_STRUCT_OFFSET modsFound 12 4 1
ENDIF

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "PrimaryKey"  iTemp0
WRITE_STRUCT_OFFSET settings 0 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "SecondaryKey" iTemp0
WRITE_STRUCT_OFFSET settings 4 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DetonatorKey" iTemp0
WRITE_STRUCT_OFFSET settings 8 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "EnableSlowMotion" iTemp0
WRITE_STRUCT_OFFSET settings 16 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "SlowMotionWithScroll" iTemp0
WRITE_STRUCT_OFFSET settings 20 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "OpenWheelWithScroll" iTemp0
WRITE_STRUCT_OFFSET settings 24 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DeselectTimeMouse" iTemp0
WRITE_STRUCT_OFFSET settings 144 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DeselectTimeScroll" iTemp0
WRITE_STRUCT_OFFSET settings 148 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DeselectTimeController" iTemp0
WRITE_STRUCT_OFFSET settings 152 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DelayToOpenWheel" iTemp0
WRITE_STRUCT_OFFSET settings 176 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "SkipEmptySlots" iTemp0
WRITE_STRUCT_OFFSET settings 40 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "InvertScrollDirection" iTemp0
WRITE_STRUCT_OFFSET settings 52 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "SetNextWeaponOnScroll" iTemp0
WRITE_STRUCT_OFFSET settings 56 4 iTemp0

READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "SlowMotionSpeed" fTemp0
WRITE_STRUCT_OFFSET settings 64 4 fTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "InvertMouseVertical" iTemp0
WRITE_STRUCT_OFFSET settings 100 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "AllowOnJetpack" iTemp0
WRITE_STRUCT_OFFSET settings 104 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "EnableDetonatorHotkey" iTemp0
WRITE_STRUCT_OFFSET settings 120 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "InvertShoulderButtons" iTemp0
WRITE_STRUCT_OFFSET settings 172 4 iTemp0

GOSUB SetSlotOrder

//Creates the user interface script
GET_THIS_SCRIPT_STRUCT iTemp0
STREAM_CUSTOM_SCRIPT WeaponWheelUI.cs 1 iTemp0 settings 0 0 isP2Instance
GET_LAST_CREATED_CUSTOM_SCRIPT child

//Disables vanilla weapon cycling without changing any flags. Dont use SET_PLAYER_CYCLE_WEAPON_BUTTON, it can conflict with missions like "Madd Dogg's Rhymes" and other mods
MAKE_NOP 0x60DA85 6
WRITE_STRUCT_OFFSET 0x60DA85 0 1 0xE9
WRITE_STRUCT_OFFSET 0x60DA85 1 4 0xFFFFFF34
MAKE_NOP 0x60D8C6 6
WRITE_STRUCT_OFFSET 0x60D8C6 0 1 0xE9
WRITE_STRUCT_OFFSET 0x60D8C6 1 4 0x000001B1

//Disables CPad::GetForceCameraBehindPlayer. Useless function, nobody missed it
WRITE_STRUCT_OFFSET 0x540AE0 0 1 0xB8
WRITE_STRUCT_OFFSET 0x540AE0 1 4 0
WRITE_STRUCT_OFFSET 0x540AE0 5 1 0xC3

main_loop:
IF GET_SCRIPT_STRUCT_NAMED REAL_M iTemp0
    WRITE_STRUCT_OFFSET modsFound 8 4 1
    currentCPAD = 0
ELSE
    WRITE_STRUCT_OFFSET modsFound 8 4 0
    currentCPAD = isP2Instance
ENDIF

iTemp0 = isP2Instance
IF GOSUB FindPlayerPed
    GET_PLAYER_CHAR isP2Instance scplayer
ELSE
    GET_DYNAMIC_LIBRARY_PROCEDURE "GetCameraControlState" vwwASI iTemp0
    CALL_FUNCTION_RETURN iTemp0 1 1 isP2Instance iTemp0

    IF iTemp0 = FALSE
        SET_CAMERA_CONTROL TRUE
    ENDIF

    GET_DYNAMIC_LIBRARY_PROCEDURE "SetWeaponWheelState" vwwASI iTemp0
    CALL_FUNCTION iTemp0 2 2 0 isP2Instance

    GET_DYNAMIC_LIBRARY_PROCEDURE "SetCameraControlState" vwwASI iTemp0
    CALL_FUNCTION iTemp0 2 2 1 isP2Instance

    GET_DYNAMIC_LIBRARY_PROCEDURE "SetCanAttackState" vwwASI iTemp0
    CALL_FUNCTION iTemp0 2 2 1 isP2Instance

    IF isP2Instance = TRUE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF

    WAIT 0
    GOTO main_loop
ENDIF

READ_STRUCT_OFFSET modsFound 0 4 iTemp0
IF isP2Instance = FALSE
AND iTemp0 = FALSE
    iTemp0 = 1
    IF GOSUB FindPlayerPed
        IF p2ScriptAddress = 0
            STREAM_CUSTOM_SCRIPT VoidWeaponWheel.cs 0 0 0 1
            GET_LAST_CREATED_CUSTOM_SCRIPT p2ScriptAddress
        ENDIF
    ELSE
        p2ScriptAddress = 0
    ENDIF
ENDIF

GET_CURRENT_CHAR_WEAPON scplayer iTemp0
GET_WEAPONTYPE_SLOT iTemp0 iTemp1
IF NOT iTemp1 = 0
    WRITE_STRUCT_OFFSET settings 180 4 iTemp0 //LastHeldWeapon
ENDIF

IF GOSUB CanOpenWheel
    IF GOSUB IsOpeningWheel
        IF NOT scrollSelect = 2
            IF wasWheelOpened = FALSE
                TIMERB = 0
            ENDIF

            READ_STRUCT_OFFSET settings 176 4 iTemp0 //DelayToOpenWheel
            IF iTemp0 > TIMERB
            AND NOT scrollSelect = 1
                wasWheelOpened = TRUE
                wheelDelayed = TRUE
                selectedUISlot = 0
                GET_DYNAMIC_LIBRARY_PROCEDURE "SetWeaponWheelState" vwwASI iTemp0
                CALL_FUNCTION iTemp0 2 2 2 isP2Instance
                WAIT 0
                GOTO main_loop
            ENDIF

            IF GOSUB CanUseSlowMotion
                IF slowMotion = FALSE
                    READ_STRUCT_OFFSET settings 64 4 fTemp0
                    SET_TIME_SCALE fTemp0
                    slowMotion = TRUE
                ENDIF
            ELSE
                //Avoids problems when releasing the button and scrolling at the same time, easily noticeable with infinite scroll mouses
                IF slowMotion = TRUE
                    SET_TIME_SCALE 1.0
                    slowMotion = FALSE
                    
                    READ_STRUCT_OFFSET settings 160 4 iTemp0
                    IF iTemp0 = FALSE
                        SET_SCRIPT_VAR child 6 0.0
                    ENDIF
                ENDIF
            ENDIF

            GET_DYNAMIC_LIBRARY_PROCEDURE "SetCameraControlState" vwwASI iTemp0

            IF scrollSelect = 0
                CALL_FUNCTION iTemp0 2 2 0 isP2Instance
                SET_CAMERA_CONTROL FALSE
            ELSE
                GET_DYNAMIC_LIBRARY_PROCEDURE "GetCameraControlState" vwwASI iTemp1
                CALL_FUNCTION_RETURN iTemp1 1 1 isP2Instance iTemp1

                IF iTemp1 = FALSE
                    CALL_FUNCTION iTemp0 2 2 1 isP2Instance
                    SET_CAMERA_CONTROL TRUE
                ENDIF
            ENDIF            

            IF GOSUB UsingController
                GOSUB GetSelectedSlotJP
            ELSE
                IF scrollSelect = 1
                    GOSUB GetSelectedSlotSCR
                ELSE
                    GOSUB GetSelectedSlotKB
                ENDIF
            ENDIF

            GET_DYNAMIC_LIBRARY_PROCEDURE "SetCanAttackState" vwwASI iTemp0
            CALL_FUNCTION iTemp0 2 2 0 isP2Instance

            GET_DYNAMIC_LIBRARY_PROCEDURE "SetWeaponWheelState" vwwASI iTemp0
            CALL_FUNCTION iTemp0 2 2 1 isP2Instance

            wasWheelOpened = TRUE
            wheelDelayed = FALSE
            SET_SCRIPT_VAR child 3 1 //ShowUI
        ENDIF
    ELSE
        //Weapon wheel could have been opened but wasn't
        //This is the branch where the weapon is set using SET_CURRENT_CHAR_WEAPON

        IF detonSelected = TRUE
            GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID iTemp0 iTemp1
            IF iTemp0 = 0
                detonSelected = FALSE
            ENDIF
        ENDIF

        //Deal with bomb/detonator hotkey
        READ_STRUCT_OFFSET settings 120 4 iTemp0
        IF iTemp0 = TRUE
            IF GOSUB CheckCameraMode
                IF GOSUB UsingController
                    GOSUB GetRealControllerMode
                    READ_STRUCT_OFFSET settings 172 4 iTemp1 //InvertShoulderButtons
                    IF iTemp1 = FALSE
                        IF iTemp0 = 0
                            iTemp0 = RIGHTSHOULDER2
                            iTemp1 = RIGHTSHOULDER1
                        ELSE
                            iTemp0 = DPADDOWN
                            iTemp1 = LEFTSHOULDER2
                        ENDIF
                    ELSE
                        IF iTemp0 = 0
                            iTemp0 = LEFTSHOULDER2
                            iTemp1 = RIGHTSHOULDER1
                        ELSE
                            iTemp0 = DPADDOWN
                            iTemp1 = LEFTSHOULDER2
                        ENDIF
                    ENDIF

                    IF IS_BUTTON_JUST_PRESSED currentCPAD iTemp0
                    AND NOT IS_BUTTON_PRESSED currentCPAD iTemp1
                        GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID iTemp1 iTemp0
                        GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID iTemp2 iTemp0
                        IF iTemp1 > 0
                        OR iTemp2 > 0
                            GOSUB UseDetonatorHotkey
                        ENDIF
                    ENDIF
                ELSE
                    READ_STRUCT_OFFSET settings 8 4 iTemp0
                    IF IS_KEY_JUST_PRESSED iTemp0
                    AND NOT IS_KEY_PRESSED VK_RBUTTON
                        GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID iTemp1 iTemp0
                        GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID iTemp2 iTemp0
                        IF iTemp1 > 0
                        OR iTemp2 > 0
                            GOSUB UseDetonatorHotkey
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF

        IF wasWheelOpened = TRUE
            IF selectedUISlot = 0
                READ_STRUCT_OFFSET settings 180 4 iTemp0
                GET_CURRENT_CHAR_WEAPON scplayer iTemp1
                GET_WEAPONTYPE_SLOT iTemp1 iTemp1

                GET_DYNAMIC_LIBRARY_PROCEDURE "GetWeaponWheelState" vwwASI iTemp2
                CALL_FUNCTION_RETURN iTemp2 1 1 isP2Instance iTemp2

                IF HAS_CHAR_GOT_WEAPON scplayer iTemp0
                AND iTemp1 = 0
                AND NOT iTemp2 = 1
                    GET_WEAPONTYPE_SLOT iTemp0 iTemp0

                    IF iTemp0 = 12
                        iTemp0 = 9
                    ELSE
                        iTemp0 ++
                    ENDIF
                ELSE
                    iTemp0 = 1
                ENDIF

                GOSUB DeTranslateSlot
            ENDIF

            GOSUB TranslateSlot
            IF weaponSlot = 9
            AND detonSelected = TRUE
                GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID iTemp0 iTemp0
                SET_CURRENT_CHAR_WEAPON scplayer weaponID
            ELSE
                GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID ammo iTemp0
                IF GOSUB IsWeaponValid
                    SET_CURRENT_CHAR_WEAPON scplayer weaponID
                ELSE
                    SET_CURRENT_CHAR_WEAPON scplayer WEAPONTYPE_UNARMED
                ENDIF
            ENDIF
        ENDIF

        GOTO AvoidRedundancy //Does the same thing anyway
    ENDIF
ELSE
    AvoidRedundancy:
    IF slowMotion = TRUE
        SET_TIME_SCALE 1.0
        slowMotion = FALSE
    ENDIF

    IF isP2Instance = TRUE
        iTemp2 = FALSE
    ELSE
        iTemp2 = TRUE
    ENDIF

    GET_DYNAMIC_LIBRARY_PROCEDURE "GetCameraControlState" vwwASI iTemp1
    CALL_FUNCTION_RETURN iTemp1 1 1 isP2Instance iTemp0
    CALL_FUNCTION_RETURN iTemp1 1 1 iTemp2 iTemp1

    IF iTemp0 = FALSE
    AND iTemp1 = TRUE
        SET_CAMERA_CONTROL TRUE
    ENDIF

    GET_DYNAMIC_LIBRARY_PROCEDURE "SetCameraControlState" vwwASI iTemp0
    CALL_FUNCTION iTemp0 2 2 1 isP2Instance

    GET_DYNAMIC_LIBRARY_PROCEDURE "SetWeaponWheelState" vwwASI iTemp0
    CALL_FUNCTION iTemp0 2 2 0 isP2Instance

    GET_DYNAMIC_LIBRARY_PROCEDURE "SetCanAttackState" vwwASI iTemp0
    CALL_FUNCTION iTemp0 2 2 1 isP2Instance

    wasWheelOpened = FALSE
    SET_SCRIPT_VAR child 3 0 //ShowUI
ENDIF

WAIT 0

GOTO main_loop

GetSelectedSlotKB:
IF wasWheelOpened = FALSE
OR wheelDelayed = TRUE
    selectedUISlot = 0
    cursorX = 0.0
    cursorY = 0.0
    angle = 0.0
ENDIF

GET_PC_MOUSE_MOVEMENT x y

IF GOSUB CheckMouseVerticalInvert
    y *= -1.0
ENDIF

x *= x
y *= y
x += y
SQRT x distance

IF distance > 1.0
    TIMERA = 0

    IF distance > 3.0
        iTemp0 = TRUE
    ENDIF
ENDIF

IF iTemp0 = TRUE
    GET_PC_MOUSE_MOVEMENT x y

    IF GOSUB CheckMouseVerticalInvert
        y *= -1.0
    ENDIF

    y *= -1.0

    cursorX += x
    cursorY += y

    x = cursorX
    x *= cursorX

    y = cursorY
    y *= cursorY

    x += y
    SQRT x distance

    cursorX *= 50.0
    cursorY *= 50.0

    IF distance = 0.0
        cursorX = 0.0
        cursorY = 0.0
    ELSE
        cursorX /= distance
        cursorY /= distance
    ENDIF

    x = cursorX
    y = cursorY

    x *= -1.0
    y *= -1.0

    fTemp0 = x
    fTemp1 = y

    GET_HEADING_FROM_VECTOR_2D x y angle
    angle /= 30.0

    GET_PC_MOUSE_MOVEMENT x y

    IF GOSUB CheckMouseVerticalInvert
        y *= -1.0
    ENDIF

    x *= x
    y *= y
    x += y
    SQRT x distance

    GET_PC_MOUSE_MOVEMENT x y

    IF GOSUB CheckMouseVerticalInvert
        y *= -1.0
    ENDIF

    x *= -1.0

    GET_HEADING_FROM_VECTOR_2D x y fTemp2
    fTemp2 /= 30.0

    GET_ANGLE_BETWEEN_2D_VECTORS fTemp0 fTemp1 x y fTemp0

    IF fTemp0 > 100.0
    AND distance > 4.5
        angle = fTemp2
        cursorX = 0.0
        cursorY = 0.0
    ENDIF

    GOSUB CalcAngle
ENDIF

GOSUB CalcDelay

IF TIMERA > deselectDelayKB
    selectedUISlot = 0
    cursorX = 0.0
    cursorY = 0.0
ENDIF

//Deal with alternating bomb/detonator slot
GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID ammo iTemp0
IF ammo > 0
    GOSUB TranslateSlot
    IF weaponSlot = 9
        READ_STRUCT_OFFSET settings 8 4 iTemp0
        IF IS_MOUSE_WHEEL_UP
        OR IS_MOUSE_WHEEL_DOWN
        OR IS_KEY_JUST_PRESSED iTemp0
            IF detonSelected = FALSE
                detonSelected = TRUE
            ELSE
                detonSelected = FALSE
            ENDIF
            TIMERA = 0
        ENDIF
    ENDIF

    GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID ammo iTemp0
    IF ammo = 0
        detonSelected = TRUE
    ENDIF
ELSE
    detonSelected = FALSE
ENDIF

RETURN

GetSelectedSlotSCR:
IF wasWheelOpened = FALSE
    GET_CURRENT_CHAR_WEAPON scplayer weaponID
    GET_WEAPONTYPE_SLOT weaponID iTemp0
    iTemp0 ++
    GOSUB DeTranslateSlot
    
    TIMERA = 0
ENDIF

READ_STRUCT_OFFSET settings 56 4 iTemp0
IF iTemp0 = 1
OR wasWheelOpened = TRUE
    IF IS_MOUSE_WHEEL_UP
    OR IS_MOUSE_WHEEL_DOWN
        IF IS_MOUSE_WHEEL_UP
            iTemp1 = -1
        ELSE
            iTemp1 = 1
        ENDIF

        READ_STRUCT_OFFSET settings 52 4 iTemp0
        IF iTemp0 = 1
            iTemp1 *= -1
        ENDIF

        READ_STRUCT_OFFSET settings 40 4 iTemp2
        next0:
        selectedUISlot += iTemp1

        IF selectedUISlot > 12
            selectedUISlot = 1
        ELSE
            IF 1 > selectedUISlot
                selectedUISlot = 12
            ENDIF
        ENDIF

        IF iTemp2 = 1
            GOSUB TranslateSlot            

            GOSUB GetWeapInSlot_SolveDetonSlot

            IF GOSUB NotIsWeaponValid
                GOTO next0
            ENDIF
        ENDIF

        TIMERA = 0
    ENDIF
ENDIF

//Deal with alternating bomb/detonator slot
GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID ammo iTemp0
IF ammo > 0
    GOSUB TranslateSlot
    IF weaponSlot = 9
        READ_STRUCT_OFFSET settings 8 4 iTemp0
        IF IS_KEY_JUST_PRESSED iTemp0
            IF detonSelected = FALSE
                detonSelected = TRUE
            ELSE
                detonSelected = FALSE
            ENDIF
            TIMERA = 0
        ENDIF
    ENDIF

    GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID ammo iTemp0
    IF ammo = 0
        detonSelected = TRUE
    ENDIF
ELSE
    detonSelected = FALSE
ENDIF

RETURN

GetSelectedSlotJP:
IF wasWheelOpened = FALSE
OR wheelDelayed = TRUE
    selectedUISlot = 0
    cursorX = 0.0
    cursorY = 0.0
    angle = 0.0
ENDIF

GET_POSITION_OF_ANALOGUE_STICKS currentCPAD iTemp0 iTemp0 iTemp0 iTemp1

x =# iTemp0
y =# iTemp1
x *= x
y *= y
x += y
SQRT x distance

IF distance > 100.0
    x =# iTemp0
    y =# iTemp1
    x *= -1.0
    y *= -1.0

    GET_HEADING_FROM_VECTOR_2D x y fTemp0
    fTemp0 /= 30.0

    TIMERA = 0
    angle = fTemp0
    iTemp0 = TRUE
ELSE
    iTemp0 = FALSE
ENDIF

IF iTemp0 = TRUE
    GOSUB CalcAngle
ENDIF

GOSUB CalcDelay

IF TIMERA > deselectDelayJP
    selectedUISlot = 0
    cursorX = 0.0
    cursorY = 0.0
ENDIF

//Deal with alternating bomb/detonator slot
GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID ammo iTemp0
IF ammo > 0    
    READ_STRUCT_OFFSET settings 172 4 iTemp1 //InvertShoulderButtons
    IF iTemp1 = FALSE
        iTemp2 = RIGHTSHOULDER2
    ELSE
        GOSUB GetRealControllerMode

        IF iTemp0 = 0
            iTemp2 = LEFTSHOULDER2
        ELSE
            iTemp2 = RIGHTSHOULDER2
        ENDIF

    ENDIF

    GOSUB TranslateSlot
    IF weaponSlot = 9
        IF IS_BUTTON_JUST_PRESSED currentCPAD iTemp2
            IF detonSelected = FALSE
                detonSelected = TRUE
            ELSE
                detonSelected = FALSE
            ENDIF
            TIMERA = 0
        ENDIF
    ENDIF

    GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID ammo iTemp0
    IF ammo = 0
        detonSelected = TRUE
    ENDIF
ELSE
    detonSelected = FALSE
ENDIF

RETURN

CalcAngle:
iTemp2 = 13
fTemp0 = angle
fTemp0 += 1.5
iTemp1 =# fTemp0

IF iTemp1 = iTemp2
    iTemp1 = 1
ENDIF

selectedUISlot = iTemp1
READ_STRUCT_OFFSET settings 40 4 iTemp0

IF iTemp0 = 1
    GOSUB GetClosestSlot
ENDIF

RETURN

GetClosestSlot:
GOSUB TranslateSlot

GOSUB GetWeapInSlot_SolveDetonSlot

IF GOSUB NotIsWeaponValid
    iTemp2 = selectedUISlot
    next1:
    selectedUISlot ++

    IF selectedUISlot > 12
        selectedUISlot = 1
    ENDIF

    GOSUB TranslateSlot
    GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID ammo iTemp0

    IF GOSUB NotIsWeaponValid
        GOTO next1
    ELSE
        iTemp1 = selectedUISlot
    ENDIF

    selectedUISlot = iTemp2
    next2:
    selectedUISlot --

    IF selectedUISlot < 1
        selectedUISlot = 12
    ENDIF

    GOSUB TranslateSlot
    GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID ammo iTemp0

    IF GOSUB NotIsWeaponValid
        GOTO next2
    ELSE
        iTemp2 = selectedUISlot
    ENDIF

    IF iTemp1 >= iTemp2
        fTemp0 =# iTemp1
        fTemp1 =# iTemp2
        fTemp2 = fTemp0
        fTemp2 -= fTemp1
        fTemp2 /= 2.0
        fTemp2 += fTemp1
        fTemp2 -= 1.0

        IF fTemp2 >= angle
            selectedUISlot = iTemp2
        ELSE
            selectedUISlot = iTemp1
        ENDIF
    ELSE
        iTemp0 = iTemp1
        iTemp0 += 12

        fTemp0 =# iTemp0
        fTemp1 =# iTemp2
        fTemp2 = fTemp0
        fTemp2 -= fTemp1
        fTemp2 /= 2.0
        fTemp2 += fTemp1
        fTemp2 -= 1.0

        fTemp1 -= 1.0

        IF fTemp1 >= angle
            angle += 12.0
        ENDIF

        IF fTemp2 >= angle
            selectedUISlot = iTemp2
        ELSE
            selectedUISlot = iTemp1
        ENDIF
    ENDIF
ENDIF

RETURN

CanOpenWheel:
GET_PED_POINTER scplayer iTemp0
READ_STRUCT_OFFSET iTemp0 252 4 iTemp0

CALL_FUNCTION_RETURN 0x53FB70 1 1 isP2Instance iTemp1
READ_STRUCT_OFFSET iTemp1 0x11D 1 iTemp1 //DisablePlayerCycleWeapon

READ_STRUCT_OFFSET settings 104 4 iTemp2
IF iTemp2 = 0
AND IS_PLAYER_USING_JETPACK isP2Instance
    RETURN_FALSE
    RETURN
ENDIF

IF NOT IS_PLAYER_CONTROL_ON isP2Instance
OR NOT IS_PLAYER_PLAYING isP2Instance
OR NOT IS_CHAR_ON_FOOT scplayer
OR NOT iTemp0 = 0
OR iTemp1 = 1
    RETURN_FALSE
    RETURN
ENDIF

IF GOSUB CheckImportantTask
    RETURN_FALSE
    RETURN
ENDIF

IF GOSUB CheckParachute
    RETURN_FALSE
    RETURN
ENDIF

IF isP2Instance = TRUE
    RETURN_TRUE
    RETURN
ENDIF

//Avoids conflicts while using (kb or scroll) and controller at the same time
IF GOSUB UsingController
    READ_STRUCT_OFFSET settings 0 4 iTemp0
    READ_STRUCT_OFFSET settings 4 4 iTemp1
    READ_STRUCT_OFFSET settings 8 4 iTemp2

    IF IS_KEY_PRESSED iTemp0
    OR IS_KEY_PRESSED iTemp1
    OR IS_KEY_PRESSED iTemp2
    OR IS_MOUSE_WHEEL_UP
    OR IS_MOUSE_WHEEL_DOWN
        RETURN_FALSE
    ELSE
        RETURN_TRUE
    ENDIF
ELSE
    RETURN_TRUE
ENDIF

RETURN

CheckImportantTask:
IF IS_CHAR_DOING_TASK_ID scplayer TASK_COMPLEX_USE_MOBILE_PHONE
OR IS_CHAR_DOING_TASK_ID scplayer TASK_COMPLEX_USE_GOGGLES
    RETURN_TRUE
ELSE
    RETURN_FALSE
ENDIF

RETURN

CheckCameraMode:
READ_STRUCT_OFFSET 0xB6F1A8 0 2 iTemp0 //Cam mode

IF NOT iTemp0 = 5 //AIMING
AND NOT iTemp0 = 7 //SNIPER
AND NOT iTemp0 = 8 //ROCKETLAUNCHER
AND NOT iTemp0 = 46 //CAMERA
AND NOT iTemp0 = 51 //ROCKETLAUNCHER_HS
AND NOT iTemp0 = 53 //AIMWEAPON
AND NOT iTemp0 = 65 //AIMWEAPON_ATTACHED
    RETURN_TRUE
ELSE
    RETURN_FALSE
ENDIF

RETURN

CheckParachute:
CLEO_CALL ReadGlobalVar 0 1513 iTemp0

IF NOT iTemp0 = 0
    RETURN_TRUE
ELSE
    RETURN_FALSE
ENDIF

RETURN

CanUseSlowMotion:
READ_STRUCT_OFFSET settings 16 4 iTemp0
READ_STRUCT_OFFSET modsFound 0 4 iTemp1

IF iTemp0 = TRUE
AND iTemp1 = FALSE
    READ_STRUCT_OFFSET modsFound 8 4 iTemp0
    IF iTemp0 = TRUE //RealM
        GOTO skip0 //Cant mix AND OR so I had to use this
    ENDIF

    IF p2ScriptAddress = 0
    AND isP2Instance = FALSE
        skip0:
        IF scrollSelect = 1
            READ_STRUCT_OFFSET settings 20 4 iTemp0

            IF iTemp0 = 1
                RETURN_TRUE
            ELSE
                RETURN_FALSE
            ENDIF
        ELSE
            RETURN_TRUE
        ENDIF
    ELSE
        RETURN_FALSE
    ENDIF
ELSE
    RETURN_FALSE
ENDIF

RETURN

IsOpeningWheel:
IF GOSUB CheckCameraMode
    IF GOSUB UsingController
        scrollSelect = 0
        GOSUB GetRealControllerMode
        READ_STRUCT_OFFSET settings 172 4 iTemp1 //InvertShoulderButtons
        IF iTemp1 = FALSE
            IF iTemp0 = 0
                iTemp1 = LEFTSHOULDER2
                iTemp2 = RIGHTSHOULDER1
            ELSE
                iTemp1 = RIGHTSHOULDER1
                iTemp2 = LEFTSHOULDER2
            ENDIF
        ELSE
            IF iTemp0 = 0
                iTemp1 = RIGHTSHOULDER2
                iTemp2 = RIGHTSHOULDER1
            ELSE
                iTemp1 = LEFTSHOULDER1
                iTemp2 = LEFTSHOULDER2
            ENDIF
        ENDIF

        IF IS_BUTTON_PRESSED currentCPAD iTemp2
            RETURN_FALSE
            RETURN
        ENDIF

        IF IS_BUTTON_PRESSED currentCPAD iTemp1
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ELSE
        IF IS_KEY_PRESSED VK_RBUTTON
            scrollSelect = 0
            RETURN_FALSE
            RETURN
        ENDIF

        READ_STRUCT_OFFSET settings 0 4 iTemp0
        READ_STRUCT_OFFSET settings 4 4 iTemp1

        IF IS_KEY_PRESSED iTemp0
        OR IS_KEY_PRESSED iTemp1
            scrollSelect = 0
            RETURN_TRUE
        ELSE
            READ_STRUCT_OFFSET settings 24 4 iTemp0

            IF iTemp0 = 1
                IF IS_MOUSE_WHEEL_UP
                OR IS_MOUSE_WHEEL_DOWN
                    scrollSelect = 1
                    RETURN_TRUE
                ELSE
                    IF scrollSelect = 1
                        GOSUB CalcDelay

                        IF TIMERA > deselectDelaySCR
                            scrollSelect = 0
                            RETURN_FALSE
                        ELSE
                            scrollSelect = 1
                            RETURN_TRUE
                        ENDIF
                    ELSE
                        RETURN_FALSE
                    ENDIF
                ENDIF
            ELSE
                IF IS_MOUSE_WHEEL_UP
                OR IS_MOUSE_WHEEL_DOWN
                    scrollSelect = 2

                    IF IS_MOUSE_WHEEL_UP
                        iTemp1 = -1
                    ELSE
                        iTemp1 = 1
                    ENDIF

                    READ_STRUCT_OFFSET settings 52 4 iTemp0

                    IF iTemp0 = 1
                        iTemp1 *= -1
                    ENDIF

                    next3:
                    selectedUISlot += iTemp1

                    IF selectedUISlot > 12
                        selectedUISlot = 1
                    ELSE
                        IF selectedUISlot < 1
                            selectedUISlot = 12
                        ENDIF
                    ENDIF

                    GOSUB TranslateSlot

                    GOSUB GetWeapInSlot_SolveDetonSlot

                    IF GOSUB NotIsWeaponValid
                        GOTO next3
                    ENDIF

                    wasWheelOpened = TRUE
                    RETURN_TRUE
                ELSE
                    RETURN_FALSE
                ENDIF
            ENDIF
        ENDIF
    ENDIF
ELSE
    RETURN_FALSE
ENDIF

RETURN

CalcDelay:
READ_STRUCT_OFFSET settings 144 4 deselectDelayKB
READ_STRUCT_OFFSET settings 148 4 deselectDelaySCR
READ_STRUCT_OFFSET settings 152 4 deselectDelayJP

IF slowMotion = TRUE
    fTemp0 =# deselectDelayKB
    READ_STRUCT_OFFSET settings 64 4 fTemp1
    fTemp0 *= fTemp1
    deselectDelayKB =# fTemp0

    READ_STRUCT_OFFSET settings 20 4 iTemp0

    IF iTemp0 = 1
        fTemp0 =# deselectDelaySCR
        fTemp0 *= fTemp1
        deselectDelaySCR =# fTemp0
    ENDIF

    fTemp0 =# deselectDelayJP
    fTemp0 *= fTemp1
    deselectDelayJP =# fTemp0
ENDIF

RETURN

CheckMouseVerticalInvert:
READ_STRUCT_OFFSET settings 100 4 iTemp0
IF NOT IS_MOUSE_USING_VERTICAL_INVERSION
OR iTemp0 = TRUE
    RETURN_TRUE
ELSE
    RETURN_FALSE
ENDIF

RETURN

UsingController:
READ_STRUCT_OFFSET modsFound 8 4 iTemp0
IF iTemp0 = TRUE //RealM
    IF IS_PC_USING_JOYPAD
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF

    RETURN
ENDIF

IF isP2Instance = TRUE
    RETURN_TRUE
ELSE
    IF NOT p2ScriptAddress = 0
        GET_DYNAMIC_LIBRARY_PROCEDURE "GetMapPadOneToPadTwo" vwwASI iTemp0
        CALL_FUNCTION_RETURN iTemp0 0 0 iTemp0

        IF iTemp0 = TRUE
            RETURN_FALSE
        ELSE
            GET_DYNAMIC_LIBRARY_PROCEDURE "GetHasPadInHands" vwwASI iTemp0
            CALL_FUNCTION_RETURN iTemp0 1 1 currentCPAD iTemp0

            IF iTemp0 = TRUE
                RETURN_TRUE
            ELSE
                RETURN_FALSE
            ENDIF
        ENDIF
    ELSE
        IF IS_PC_USING_JOYPAD
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ENDIF
ENDIF

RETURN

GetRealControllerMode:
IF currentCPAD = 0
    GET_CONTROLLER_MODE iTemp0
ELSE
    READ_STRUCT_OFFSET 0xB7358C 0x10A 2 iTemp0
ENDIF

RETURN

FindPlayerPed:
CALL_FUNCTION_RETURN 0x56E210 1 1 iTemp0 iTemp0 //FindPlayerPed

IF iTemp0 = 0
    RETURN_FALSE
ELSE    
    RETURN_TRUE
ENDIF

RETURN

IsWeaponValid:
IF weaponID = 0
    IF weaponSlot = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
ELSE
    IF weaponSlot = 9
        IF ammo = 0
            RETURN_FALSE
        ELSE
            RETURN_TRUE
        ENDIF
    ELSE
        READ_STRUCT_OFFSET modsFound 4 4 iTemp0
        
        IF ammo = 0
        AND iTemp0 = FALSE //KeepNoAmmo
            RETURN_FALSE
        ELSE
            RETURN_TRUE
        ENDIF
    ENDIF
ENDIF

RETURN

NotIsWeaponValid:
IF GOSUB IsWeaponValid
    RETURN_FALSE
ELSE
    RETURN_TRUE
ENDIF

RETURN

GetWeapInSlot_SolveDetonSlot:
IF weaponSlot = 9
AND detonSelected = TRUE
    GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID ammo iTemp0
ELSE
    GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID ammo iTemp0
ENDIF

RETURN

UseDetonatorHotkey:
wasWheelOpened = TRUE
GOSUB TranslateSlot

IF weaponSlot = 9
    IF detonSelected = TRUE
        IF iTemp1 > 0
            detonSelected = FALSE
        ENDIF
    ELSE
        IF iTemp2 > 0
            detonSelected = TRUE
        ENDIF
    ENDIF
ELSE
    iTemp0 = 9
    GOSUB DeTranslateSlot
    GOSUB TranslateSlot

    GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID iTemp2 iTemp0

    IF iTemp2 > 0
        detonSelected = TRUE
    ELSE
        detonSelected = FALSE
    ENDIF
ENDIF

RETURN

TranslateSlot:
READ_STRUCT_OFFSET settings 12 4 iTemp0
weaponSlot = selectedUISlot - 1
weaponSlot *= 4
READ_STRUCT_OFFSET iTemp0 weaponSlot 4 weaponSlot

RETURN

DeTranslateSlot:
READ_STRUCT_OFFSET settings 12 4 iTemp1

REPEAT 12 ammo //Used ammo because of lack of variables
    iTemp2 = ammo
    iTemp2 *= 4
    READ_STRUCT_OFFSET iTemp1 iTemp2 4 iTemp2

    IF iTemp0 = iTemp2
        selectedUISlot = ammo
        selectedUISlot ++
    ENDIF
ENDREPEAT

RETURN

SetSlotOrder:
GET_LABEL_POINTER SlotOrder iTemp0
WRITE_STRUCT_OFFSET settings 12 4 iTemp0

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_1" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_2" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_3" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_4" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_5" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_6" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_7" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_8" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_9" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_10" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_11" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE
iTemp0 += 4
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "slots" "WeaponTypeOnSlot_12" iTemp1
WRITE_MEMORY iTemp0 4 iTemp1 FALSE

RETURN

SCRIPT_END
}

{
LVAR_INT var //In
LVAR_INT value scriptSpace

ReadGlobalVar:
READ_MEMORY 0x468D5E 4 1 scriptSpace
READ_STRUCT_PARAM scriptSpace var value

CLEO_RETURN 0 value
}

ModsFound:
DUMP
00 00 00 00 //0 SAMP
00 00 00 00 //4 KeepNoAmmo
00 00 00 00 //8 RealM
00 00 00 00 //12 GInput
ENDDUMP

Settings:
DUMP
00 00 00 00 //0 PrimaryKey
00 00 00 00 //4 SecondaryKey
00 00 00 00 //8 DetonatorKey
00 00 00 00 //12 SlotOrder
00 00 00 00 //16 EnableSlowMotion
00 00 00 00 //20 SlowMotionWithScroll
00 00 00 00 //24 OpenWheelWithScroll
00 00 00 00 //28 DetailR
00 00 00 00 //32 DetailG
00 00 00 00 //36 DetailB
00 00 00 00 //40 SkipEmptySlots
00 00 00 00 //44 AmmoCounterLimit
00 00 00 00 //48 FlamethrowerAmmoFix
00 00 00 00 //52 InvertScrollDirection
00 00 00 00 //56 SetNextWeaponOnScroll
00 00 00 00 //60 DetailAnimSpeed
00 00 00 00 //64 SlowMotionSpeed
00 00 00 00 //68 StretchIconVertically
00 00 00 00 //72 EnableCustomIcons
00 00 00 00 //76 CenterX
00 00 00 00 //80 CenterY
00 00 00 00 //84 WheelRadius
00 00 00 00 //88 CenterXMultiplayer
00 00 00 00 //92 CenterYMultiplayer
00 00 00 00 //96 WheelRadiusMultiplayer
00 00 00 00 //100 InvertMouseVertical
00 00 00 00 //104 AllowOnJetpack
00 00 00 00 //108 EnableSounds
00 00 00 00 //112 CustomSoundsVolume
00 00 00 00 //116 UseCustomSounds
00 00 00 00 //120 EnableDetonatorHotkey
00 00 00 00 //124 ModsFound
00 00 00 00 //128 EnableFilter
00 00 00 00 //132 FilterR
00 00 00 00 //136 FilterG
00 00 00 00 //140 FilterB
00 00 00 00 //144 DeselectTimeMouse
00 00 00 00 //148 DeselectTimeScroll
00 00 00 00 //152 DeselectTimeController
00 00 00 00 //156 FilterAnimSpeed
00 00 00 00 //160 FilterOnScroll
00 00 00 00 //164 InvertCenterXForPlayer2
00 00 00 00 //168 InvertCenterYForPlayer2
00 00 00 00 //172 InvertShoulderButtons
00 00 00 00 //176 DelayToOpenWheel
00 00 00 00 //180 LastHeldWeapon
00 00 00 00 //184 FontSizeX
00 00 00 00 //188 FontSizeY
00 00 00 00 //192 WeaponNameFontStyle
00 00 00 00 //196 AmmoFontStyle
ENDDUMP

SlotOrder:
DUMP
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
00 00 00 00
ENDDUMP