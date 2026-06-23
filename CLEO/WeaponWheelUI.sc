SCRIPT_START
{
NOP

SCRIPT_NAME VWWUI

LVAR_INT iTemp0 parent settings showUI detonSelected isP2Instance

LVAR_FLOAT filterAlpha

LVAR_INT scplayer iTemp1 iTemp2 iTemp3 wasWheelOpened texture weaponID selectedUISlot weaponSlot lastSelectedSlot ammo totalAmmo openSFX closeSFX selectSFX

LVAR_FLOAT fTemp0 fTemp1 sizeX sizeY scaleMultiplier centerX centerY wheelRadius weaponIconSize detailTimer

IF iTemp0 = FALSE //Terminate if wasn't created by parent script
    TERMINATE_THIS_CUSTOM_SCRIPT
ENDIF

READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DetailR" iTemp0
WRITE_STRUCT_OFFSET settings 28 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DetailG" iTemp0
WRITE_STRUCT_OFFSET settings 32 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DetailB" iTemp0
WRITE_STRUCT_OFFSET settings 36 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "AmmoCounterLimit" iTemp0
WRITE_STRUCT_OFFSET settings 44 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FlamethrowerAmmoFix" iTemp0
WRITE_STRUCT_OFFSET settings 48 4 iTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "DetailAnimSpeed" fTemp0
WRITE_STRUCT_OFFSET settings 60 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "CenterX" fTemp0
WRITE_STRUCT_OFFSET settings 76 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "CenterY" fTemp0
WRITE_STRUCT_OFFSET settings 80 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "WheelRadius" fTemp0
WRITE_STRUCT_OFFSET settings 84 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "CenterXMultiplayer" fTemp0
WRITE_STRUCT_OFFSET settings 88 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "CenterYMultiplayer" fTemp0
WRITE_STRUCT_OFFSET settings 92 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "WheelRadiusMultiplayer" fTemp0
WRITE_STRUCT_OFFSET settings 96 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "WeaponIconSize" weaponIconSize
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "StretchIconVertically" iTemp0
WRITE_STRUCT_OFFSET settings 68 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "EnableCustomIcons" iTemp0
WRITE_STRUCT_OFFSET settings 72 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "EnableSounds" iTemp0
WRITE_STRUCT_OFFSET settings 108 4 iTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "CustomSoundsVolume" fTemp0
WRITE_STRUCT_OFFSET settings 112 4 fTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "UseCustomSounds" iTemp0
WRITE_STRUCT_OFFSET settings 116 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "EnableFilter" iTemp0
WRITE_STRUCT_OFFSET settings 128 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FilterR" iTemp0
WRITE_STRUCT_OFFSET settings 132 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FilterG" iTemp0
WRITE_STRUCT_OFFSET settings 136 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FilterB" iTemp0
WRITE_STRUCT_OFFSET settings 140 4 iTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FilterAnimSpeed" fTemp0
WRITE_STRUCT_OFFSET settings 156 4 fTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FilterOnScroll" iTemp0
WRITE_STRUCT_OFFSET settings 160 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "InvertCenterXForPlayer2" iTemp0
WRITE_STRUCT_OFFSET settings 164 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "InvertCenterYForPlayer2" iTemp0
WRITE_STRUCT_OFFSET settings 168 4 iTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FontSizeX" fTemp0
WRITE_STRUCT_OFFSET settings 184 4 fTemp0
READ_FLOAT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "FontSizeY" fTemp0
WRITE_STRUCT_OFFSET settings 188 4 fTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "WeaponNameFontStyle" iTemp0
WRITE_STRUCT_OFFSET settings 192 4 iTemp0
READ_INT_FROM_INI_FILE "cleo/VoidWeaponWheel.ini" "configs" "AmmoFontStyle" iTemp0
WRITE_STRUCT_OFFSET settings 196 4 iTemp0
LOAD_TEXTURE_DICTIONARY SLOTVWW
LOAD_SPRITE 1 "SLOT0"
LOAD_SPRITE 2 "SLOT1"

READ_STRUCT_OFFSET settings 72 4 iTemp0
IF iTemp0 = TRUE
    LOAD_TEXTURE_DICTIONARY WEAPVWW
    LOAD_SPRITE 3 "fist"
ENDIF
READ_STRUCT_OFFSET settings 108 4 iTemp0
READ_STRUCT_OFFSET settings 116 4 iTemp1
IF iTemp0 = TRUE
AND iTemp1 = TRUE
    LOAD_AUDIO_STREAM "cleo/audio/openvww.mp3" openSFX
    LOAD_AUDIO_STREAM "cleo/audio/closevww.mp3" closeSFX
    LOAD_AUDIO_STREAM "cleo/audio/selectvww.mp3" selectSFX
ENDIF

main_loop:
WAIT 0

iTemp0 = isP2Instance
IF GOSUB FindPlayerPed
    GET_PLAYER_CHAR isP2Instance scplayer
ELSE
    IF isP2Instance = TRUE
        READ_STRUCT_OFFSET settings 108 4 iTemp0
        READ_STRUCT_OFFSET settings 116 4 iTemp1
        IF iTemp0 = TRUE
        AND iTemp1 = TRUE
            REMOVE_AUDIO_STREAM openSFX
            REMOVE_AUDIO_STREAM closeSFX
            REMOVE_AUDIO_STREAM selectSFX
        ENDIF
        
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF

    GOTO main_loop
ENDIF

//Update custom sounds volume
READ_STRUCT_OFFSET settings 108 4 iTemp0
READ_STRUCT_OFFSET settings 116 4 iTemp1
IF iTemp0 = 1
AND iTemp1 = 1
    GET_AUDIO_SFX_VOLUME fTemp0
    READ_STRUCT_OFFSET settings 112 4 fTemp1
    fTemp0 *= fTemp1
    SET_AUDIO_STREAM_VOLUME openSFX fTemp0
    SET_AUDIO_STREAM_VOLUME closeSFX fTemp0
    SET_AUDIO_STREAM_VOLUME selectSFX fTemp0
ENDIF

IF showUI = TRUE
    READ_STRUCT_OFFSET settings 124 4 iTemp0
    READ_STRUCT_OFFSET iTemp0 8 4 iTemp0
    IF iTemp0 = TRUE //RealM
        GOTO skip0 //Cant mix GOSUB AND OR so I had to use this
    ENDIF

    iTemp0 = 1
    IF GOSUB NotFindPlayerPed
        skip0:
        READ_STRUCT_OFFSET settings 128 4 iTemp0
        IF iTemp0 = 1
            GET_SCRIPT_VAR parent 4 iTemp0
            READ_STRUCT_OFFSET settings 160 4 iTemp1
            IF iTemp0 = 0
            OR iTemp1 = 1
                IF 90.0 > filterAlpha
                    GET_SCRIPT_VAR parent 2 iTemp0
                    READ_STRUCT_OFFSET settings 156 4 fTemp0
                    IF iTemp0 = 1
                        READ_STRUCT_OFFSET settings 64 4 fTemp1
                        fTemp0 /= fTemp1
                    ENDIF

                    filterAlpha +=@ fTemp0
                    CLAMP_FLOAT filterAlpha 0.0 90.0 filterAlpha
                ENDIF

                iTemp3 =# filterAlpha
                READ_STRUCT_OFFSET settings 132 4 iTemp0
                READ_STRUCT_OFFSET settings 136 4 iTemp1
                READ_STRUCT_OFFSET settings 140 4 iTemp2
                DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD 320.0 224.0 642.0 450.0 0.0 0.0 FALSE 0 0 iTemp0 iTemp1 iTemp2 iTemp3
            ENDIF
        ENDIF
    ENDIF

    IF wasWheelOpened = 0
        READ_STRUCT_OFFSET settings 108 4 iTemp0
        IF iTemp0 = 1
            READ_STRUCT_OFFSET settings 116 4 iTemp1
            IF iTemp1 = 1
                SET_AUDIO_STREAM_STATE openSFX 1
            ELSE
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 0.0 1
            ENDIF
        ENDIF
    ENDIF

    GOSUB GetProperPos
    GET_SCRIPT_VAR parent 0 selectedUISlot    

    IF selectedUISlot = 0
        weaponSlot = 0
    ELSE
        GOSUB TranslateSlot0
        IF NOT selectedUISlot = lastSelectedSlot
            detailTimer = 0.0
            
            READ_STRUCT_OFFSET settings 108 4 iTemp0
            IF iTemp0 = TRUE
                READ_STRUCT_OFFSET settings 116 4 iTemp1
                IF iTemp1 = TRUE
                    IF TIMERA > 20
                        SET_AUDIO_STREAM_STATE selectSFX 1
                        TIMERA = 0
                    ENDIF
                ELSE
                    CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 0.0 3
                ENDIF
            ENDIF
        ENDIF
    ENDIF

    lastSelectedSlot = selectedUISlot
    GET_SCRIPT_VAR parent 1 detonSelected
    GET_TEXTURE_FROM_SPRITE 1 texture
    IF 1.0 > detailTimer
        GET_SCRIPT_VAR parent 2 iTemp0
        READ_STRUCT_OFFSET settings 60 4 fTemp0
        IF iTemp0 = 1
            READ_STRUCT_OFFSET settings 64 4 fTemp1
            fTemp0 /= fTemp1
        ENDIF

        fTemp0 /= 16.0
        detailTimer +=@ fTemp0
        CLAMP_FLOAT detailTimer 0.0 1.0 detailTimer
    ENDIF

    GOSUB DrawWeaponWheel
    IF NOT selectedUISlot = 0
        IF weaponSlot = 9
        AND detonSelected = 1
            GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID totalAmmo iTemp0
        ELSE
            GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID totalAmmo iTemp0
        ENDIF

        GET_PED_POINTER scplayer iTemp2

        iTemp0 = weaponSlot - 1

        iTemp0 *= 0x1C //sizeof CWeapon
        iTemp0 += iTemp2
        iTemp3 = iTemp0 + 0x5A0 //CWeapon

        READ_STRUCT_OFFSET iTemp3 0x8 4 ammo //m_nAmmoInClip

        CALL_METHOD_RETURN 0x5E3B60 iTemp2 1 0 weaponID iTemp0 //CPed::GetWeaponSkill

        CALL_FUNCTION_RETURN 0x743C60 2 2 iTemp0 weaponID iTemp3 //CWeaponInfo::GetWeaponInfo

        READ_STRUCT_OFFSET iTemp3 0x20 2 iTemp2

        iTemp0 = weaponSlot
        IF GOSUB IsWeaponValid
            fTemp0 = 9.0
            fTemp0 *= scaleMultiplier
            fTemp1 = centerY
            fTemp1 -= fTemp0            
            READ_STRUCT_OFFSET settings 184 4 sizeX
            sizeX *= 0.6
            READ_STRUCT_OFFSET settings 188 4 sizeY
            sizeY *= 0.6
            sizeX *= scaleMultiplier
            sizeY *= scaleMultiplier
            GOSUB GetWeaponName
            READ_STRUCT_OFFSET settings 192 4 iTemp1
            DRAW_STRING_EXT $iTemp0 DRAW_EVENT_BEFORE_HUD centerX fTemp1 sizeX sizeY TRUE iTemp1 TRUE ALIGN_CENTER 500.0 FALSE 255 255 255 255 1 0 0 0 0 255 FALSE 0 0 0 0
            IF iTemp2 > 1
                GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID totalAmmo iTemp0
                totalAmmo -= ammo
                READ_STRUCT_OFFSET settings 44 4 iTemp2
                IF 10000 > totalAmmo
                OR iTemp2 = 0
                    READ_STRUCT_OFFSET settings 48 4 iTemp2
                    IF weaponID = 37
                    AND iTemp2 = 1
                        totalAmmo /= 10
                        ammo /= 10
                    ENDIF

                    fTemp0 = 6.0
                    fTemp0 *= scaleMultiplier
                    fTemp1 = centerY
                    fTemp1 += fTemp0
                    READ_STRUCT_OFFSET settings 184 4 sizeX
                    sizeX *= 0.6
                    READ_STRUCT_OFFSET settings 188 4 sizeY
                    sizeY *= 0.6
                    sizeX *= scaleMultiplier
                    sizeY *= scaleMultiplier
                    GET_LABEL_POINTER Buffer iTemp0
                    STRING_FORMAT iTemp0 "%d  -  %d" totalAmmo ammo
                    READ_STRUCT_OFFSET settings 196 4 iTemp1
                    DRAW_STRING_EXT $iTemp0 DRAW_EVENT_BEFORE_HUD centerX fTemp1 sizeX sizeY TRUE iTemp1 TRUE ALIGN_CENTER 150.0 FALSE 255 255 255 255 1 0 0 0 0 255 FALSE 0 0 0 0
                ENDIF
            ELSE
                IF NOT weaponSlot = 1
                AND NOT weaponSlot = 2
                AND NOT weaponSlot = 11
                AND NOT weaponSlot = 12
                    IF weaponSlot = 9
                    AND detonSelected = 1
                        GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID totalAmmo iTemp0
                        CLAMP_INT totalAmmo 1 32 totalAmmo
                        fTemp0 = 6.0
                        fTemp0 *= scaleMultiplier
                        fTemp1 = centerY
                        fTemp1 += fTemp0
                        READ_STRUCT_OFFSET settings 184 4 sizeX
                        sizeX *= 0.6
                        READ_STRUCT_OFFSET settings 188 4 sizeY
                        sizeY *= 0.6
                        sizeX *= scaleMultiplier
                        sizeY *= scaleMultiplier
                        GET_LABEL_POINTER Buffer iTemp0
                        STRING_FORMAT iTemp0 "%d" totalAmmo
                        READ_STRUCT_OFFSET settings 196 4 iTemp1
                        DRAW_STRING_EXT $iTemp0 DRAW_EVENT_BEFORE_HUD centerX fTemp1 sizeX sizeY TRUE iTemp1 TRUE ALIGN_CENTER 150.0 FALSE 255 255 255 255 1 0 0 0 0 255 FALSE 0 0 0 0
                    ELSE
                        GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID totalAmmo iTemp0
                        READ_STRUCT_OFFSET settings 44 4 iTemp2
                        IF 10000 > totalAmmo
                        OR iTemp2 = 0
                            fTemp0 = 6.0
                            fTemp0 *= scaleMultiplier
                            fTemp1 = centerY
                            fTemp1 += fTemp0
                            READ_STRUCT_OFFSET settings 184 4 sizeX
                            sizeX *= 0.6
                            READ_STRUCT_OFFSET settings 188 4 sizeY
                            sizeY *= 0.6
                            sizeX *= scaleMultiplier
                            sizeY *= scaleMultiplier
                            GET_LABEL_POINTER Buffer iTemp0
                            STRING_FORMAT iTemp0 "%d" totalAmmo
                            READ_STRUCT_OFFSET settings 196 4 iTemp1
                            DRAW_STRING_EXT $iTemp0 DRAW_EVENT_BEFORE_HUD centerX fTemp1 sizeX sizeY TRUE iTemp1 TRUE ALIGN_CENTER 150.0 FALSE 255 255 255 255 1 0 0 0 0 255 FALSE 0 0 0 0
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
ELSE
    IF wasWheelOpened = 1
        READ_STRUCT_OFFSET settings 108 4 iTemp0
        IF iTemp0 = 1
            READ_STRUCT_OFFSET settings 116 4 iTemp1
            IF iTemp1 = 1
                SET_AUDIO_STREAM_STATE closeSFX 1
            ELSE
                CALL_METHOD 0x506EA0 0xB6BC90 3 0 1.0 0.0 2
            ENDIF
        ENDIF
    ENDIF

    detailTimer = 0.0
    filterAlpha = 0.0

    GET_CURRENT_CHAR_WEAPON scplayer iTemp0
    GET_WEAPONTYPE_SLOT iTemp0 iTemp0

    iTemp0 ++

    GOSUB DeTranslateSlot

    lastSelectedSlot = selectedUISlot
ENDIF

wasWheelOpened = showUI

GOTO main_loop

DrawWeaponWheel:
REPEAT 12 iTemp3
    GET_TEXTURE_FROM_SPRITE 1 texture
    GOSUB DrawSlot
    iTemp0 = iTemp3
    GOSUB TranslateSlot1
    IF iTemp0 = 1
        GET_CHAR_WEAPON_IN_SLOT scplayer 1 weaponID totalAmmo iTemp2
        IF weaponID = 0
            READ_STRUCT_OFFSET 0xBAB1FC 0 4 texture //Get "fist" RwTexture from models/hud.txd

            READ_STRUCT_OFFSET settings 72 4 iTemp0
            IF iTemp0 = TRUE
                GET_TEXTURE_FROM_SPRITE 3 iTemp0

                IF NOT iTemp0 = 0
                    texture = iTemp0
                ENDIF
            ENDIF
        ELSE
            GOSUB GetWeaponIcon
        ENDIF

        iTemp0 = iTemp3
        sizeX = 1.5
        sizeX *= scaleMultiplier
        sizeX += wheelRadius
        GOSUB GetCalculatedSinCosPosition
        sizeX = weaponIconSize
        sizeX *= -1.0
        sizeY = weaponIconSize
        GOSUB DrawWeaponIcon
    ELSE
        IF iTemp0 = 9
            IF detonSelected = 1
                GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID totalAmmo iTemp2
                IF NOT totalAmmo = 0
                AND NOT weaponID = 0
                    IF GOSUB GetWeaponIcon
                        iTemp0 = iTemp3
                        sizeX = 1.5
                        sizeX *= scaleMultiplier
                        sizeX += wheelRadius
                        GOSUB GetCalculatedSinCosPosition
                        sizeX = weaponIconSize
                        sizeX *= -1.0
                        sizeY = weaponIconSize
                        GOSUB DrawWeaponIcon
                    ENDIF
                ENDIF

                IF weaponSlot = 9
                    GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID totalAmmo iTemp2
                    IF NOT totalAmmo = 0
                    AND NOT weaponID = 0
                        IF GOSUB GetWeaponIcon
                            iTemp0 = iTemp3
                            fTemp0 = 47.0
                            fTemp0 *= scaleMultiplier
                            sizeX = wheelRadius
                            sizeX -= fTemp0
                            GOSUB GetCalculatedSinCosPosition
                            sizeX = weaponIconSize
                            sizeX *= -0.6
                            sizeY = weaponIconSize
                            sizeY *= 0.6
                            GOSUB DrawWeaponIcon
                        ENDIF
                    ENDIF
                ENDIF
            ELSE
                GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID totalAmmo iTemp2
                IF NOT totalAmmo = 0
                AND NOT weaponID = 0
                    IF GOSUB GetWeaponIcon
                        iTemp0 = iTemp3
                        sizeX = 1.5
                        sizeX *= scaleMultiplier
                        sizeX += wheelRadius
                        GOSUB GetCalculatedSinCosPosition
                        sizeX = weaponIconSize
                        sizeX *= -1.0
                        sizeY = weaponIconSize
                        GOSUB DrawWeaponIcon
                    ENDIF
                ENDIF

                IF weaponSlot = 9
                    GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID totalAmmo iTemp2
                    IF NOT totalAmmo = 0
                    AND NOT weaponID = 0
                        IF GOSUB GetWeaponIcon
                            iTemp0 = iTemp3
                            fTemp0 = 47.0
                            fTemp0 *= scaleMultiplier
                            sizeX = wheelRadius
                            sizeX -= fTemp0
                            GOSUB GetCalculatedSinCosPosition
                            sizeX = weaponIconSize
                            sizeX *= -0.6
                            sizeY = weaponIconSize
                            sizeY *= 0.6
                            GOSUB DrawWeaponIcon
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ELSE
            GET_CHAR_WEAPON_IN_SLOT scplayer iTemp0 weaponID totalAmmo iTemp2
            IF GOSUB IsWeaponValid
                IF GOSUB GetWeaponIcon
                    iTemp0 = iTemp3
                    sizeX = 1.5 * scaleMultiplier
                    sizeX += wheelRadius
                    GOSUB GetCalculatedSinCosPosition
                    sizeX = weaponIconSize * -1.0
                    sizeY = weaponIconSize
                    GOSUB DrawWeaponIcon
                ENDIF
            ENDIF
        ENDIF
    ENDIF
ENDREPEAT

RETURN

GetCalculatedSinCosPosition:
fTemp0 = 360.0 / 12.0
fTemp1 =# iTemp0
fTemp1 -= 3.0
fTemp1 *= fTemp0
COS fTemp1 fTemp0
fTemp0 *= sizeX
SIN fTemp1 fTemp1
fTemp1 *= sizeX

RETURN 

DrawWeaponIcon:
GET_FIXED_XY_ASPECT_RATIO fTemp0 fTemp1 fTemp0 fTemp1
fTemp0 += centerX
fTemp1 += centerY
READ_STRUCT_OFFSET settings 68 4 iTemp0
IF iTemp0 = 1
    sizeY *= 1.2
ENDIF

sizeX *= scaleMultiplier
sizeY *= scaleMultiplier
iTemp0 = iTemp3
GOSUB TranslateSlot1
GET_CHAR_WEAPON_IN_SLOT scplayer iTemp0 weaponID ammo iTemp1
IF ammo = 0
AND NOT iTemp0 = 1
    IF iTemp0 = 9
    AND detonSelected = 1
        iTemp0 = 255
    ELSE
        iTemp0 = 100
    ENDIF
ELSE
    iTemp0 = 255
ENDIF

DRAW_TEXTURE_PLUS texture DRAW_EVENT_AFTER_HUD fTemp0 fTemp1 sizeX sizeY 0.0 0.0 TRUE 0 0 iTemp0 iTemp0 iTemp0 255

RETURN 

DrawSlot:
iTemp0 = iTemp3
sizeX = 4.5
sizeX *= detailTimer
sizeX *= scaleMultiplier
sizeX += wheelRadius
GOSUB GetCalculatedSinCosPosition
sizeX = 119.0
sizeX *= scaleMultiplier
GET_FIXED_XY_ASPECT_RATIO fTemp0 fTemp1 fTemp0 fTemp1
fTemp0 += centerX
fTemp1 += centerY
sizeY =# iTemp3
sizeY *= 30.0
iTemp0 = selectedUISlot
iTemp0 --
IF iTemp3 = iTemp0
    READ_STRUCT_OFFSET settings 28 4 iTemp0
    READ_STRUCT_OFFSET settings 32 4 iTemp1
    READ_STRUCT_OFFSET settings 36 4 iTemp2
    sizeX = 3.7
    sizeX *= detailTimer
    sizeX += 119.0
    sizeX *= scaleMultiplier
    GET_TEXTURE_FROM_SPRITE 2 texture
    DRAW_TEXTURE_PLUS texture DRAW_EVENT_AFTER_HUD fTemp0 fTemp1 sizeX sizeX sizeY 0.0 TRUE 0 0 iTemp0 iTemp1 iTemp2 255
    iTemp0 = iTemp3
    sizeX = wheelRadius
    GOSUB GetCalculatedSinCosPosition
    sizeX = 119.0
    sizeX *= scaleMultiplier
    GET_FIXED_XY_ASPECT_RATIO fTemp0 fTemp1 fTemp0 fTemp1
    fTemp0 += centerX
    fTemp1 += centerY
    GET_TEXTURE_FROM_SPRITE 1 texture
    DRAW_TEXTURE_PLUS texture DRAW_EVENT_AFTER_HUD fTemp0 fTemp1 sizeX sizeX sizeY 0.0 TRUE 0 0 234 234 234 255
    IF weaponSlot = 9
        iTemp0 = iTemp3
        sizeX = -48.0
        sizeX *= scaleMultiplier
        sizeX += wheelRadius
        GOSUB GetCalculatedSinCosPosition
        sizeX = 79.0
        sizeX *= scaleMultiplier
        GET_FIXED_XY_ASPECT_RATIO fTemp0 fTemp1 fTemp0 fTemp1
        fTemp0 += centerX
        fTemp1 += centerY
        IF detonSelected = 1
            GET_CHAR_WEAPON_IN_SLOT scplayer 9 weaponID totalAmmo iTemp0
            IF totalAmmo > 0
                DRAW_TEXTURE_PLUS texture DRAW_EVENT_AFTER_HUD fTemp0 fTemp1 sizeX sizeX sizeY 0.0 TRUE 0 0 0 0 0 160
            ENDIF
        ELSE
            GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID totalAmmo iTemp0
            IF totalAmmo > 0
                DRAW_TEXTURE_PLUS texture DRAW_EVENT_AFTER_HUD fTemp0 fTemp1 sizeX sizeX sizeY 0.0 TRUE 0 0 0 0 0 160
            ENDIF
        ENDIF
    ENDIF
ELSE
    iTemp0 = iTemp3
    sizeX = wheelRadius
    GOSUB GetCalculatedSinCosPosition
    sizeX = 119.0
    sizeX *= scaleMultiplier
    GET_FIXED_XY_ASPECT_RATIO fTemp0 fTemp1 fTemp0 fTemp1
    fTemp0 += centerX
    fTemp1 += centerY
    DRAW_TEXTURE_PLUS texture DRAW_EVENT_AFTER_HUD fTemp0 fTemp1 sizeX sizeX sizeY 0.0 TRUE 0 0 0 0 0 160
ENDIF

RETURN

GetProperPos:
READ_STRUCT_OFFSET settings 124 4 iTemp0
READ_STRUCT_OFFSET iTemp0 8 4 iTemp0
IF iTemp0 = TRUE //RealM
    GOTO skip1 //Cant mix GOSUB AND OR so I had to use this
ENDIF

iTemp0 = 1
IF GOSUB NotFindPlayerPed
    skip1:
    READ_STRUCT_OFFSET settings 76 4 centerX
    READ_STRUCT_OFFSET settings 80 4 centerY
    READ_STRUCT_OFFSET settings 84 4 wheelRadius
ELSE
    READ_STRUCT_OFFSET settings 88 4 centerX
    READ_STRUCT_OFFSET settings 92 4 centerY
    READ_STRUCT_OFFSET settings 96 4 wheelRadius
    IF isP2Instance = TRUE
        READ_STRUCT_OFFSET settings 164 4 iTemp0
        IF iTemp0 = 1
            fTemp0 = centerX
            centerX = 640.0
            centerX -= fTemp0
        ENDIF

        READ_STRUCT_OFFSET settings 168 4 iTemp0
        IF iTemp0 = 1
            fTemp0 = centerY
            centerY = 448.0
            centerY -= fTemp0
        ENDIF
    ENDIF
ENDIF

scaleMultiplier = wheelRadius
scaleMultiplier /= 145.0

RETURN 

GetWeaponIcon:
READ_STRUCT_OFFSET settings 72 4 iTemp0
IF iTemp0 = TRUE
    CALL_FUNCTION_RETURN 0x403DA0 1 1 iTemp2 iTemp0 //Get CBaseModelInfo from weapon model
    READ_STRUCT_OFFSET iTemp0 4 4 iTemp1 //Get the hash of the model name
    CALL_FUNCTION_RETURN 0x53CF70 2 2 "ICON" iTemp1 iTemp1 //Add the hash of "ICON" to the end of the previous hash, resulting in the hash of the icon name

    CALL_FUNCTION_RETURN 0x731850 1 1 "weapvww" iTemp0 //CTxdStore::FindTxdSlot

    IF NOT iTemp0 = -1
        CALL_FUNCTION_RETURN 0x408340 1 1 iTemp0 iTemp0 //Get the RwTexDictionary from the txd

        IF NOT iTemp0 = 0
            CALL_FUNCTION_RETURN 0x734E50 2 2 iTemp1 iTemp0 iTemp0 //Search for the RwRaster of the textura. RwTexDictionaryFindHashNamedTextur
            IF NOT iTemp0 = 0
                READ_STRUCT_OFFSET iTemp0 0 4 texture
                RETURN_TRUE
                
                RETURN
            ENDIF
        ENDIF
    ENDIF
ENDIF

CALL_FUNCTION_RETURN 0x403DA0 1 1 iTemp2 iTemp0 //Get CBaseModelInfo from weapon model
READ_STRUCT_OFFSET iTemp0 4 4 iTemp1 //Get the hash of the model name
CALL_FUNCTION_RETURN 0x53CF70 2 2 "ICON" iTemp1 iTemp1 //Add the hash of "ICON" to the end of the previous hash, resulting in the hash of the icon name

READ_STRUCT_OFFSET iTemp0 10 2 iTemp0 //Slot index of the weapon txd in the TexDictionaryPool
CALL_FUNCTION_RETURN 0x408340 1 1 iTemp0 iTemp0 //Get the RwTexDictionary from the txd

IF NOT iTemp0 = 0
    READ_STRUCT_OFFSET 0x58D84C 0 4 iTemp2 //WeaponIconsTXD mod compatibility. RwTexDictionaryFindHashNamedTextur
    iTemp2 += 0x58D850

    CALL_FUNCTION_RETURN iTemp2 2 2 iTemp1 iTemp0 iTemp0
    IF iTemp0 = 0
        RETURN_FALSE
    ELSE
        READ_STRUCT_OFFSET iTemp0 0 4 texture
        RETURN_TRUE
    ENDIF
ELSE
    RETURN_FALSE
ENDIF

RETURN

GetWeaponName:
IF NOT selectedUISlot = 0
    IF weaponSlot = 9
    AND detonSelected = 1
        GET_CHAR_WEAPON_IN_SLOT scplayer 13 weaponID iTemp0 iTemp0
    ELSE
        GET_CHAR_WEAPON_IN_SLOT scplayer weaponSlot weaponID iTemp0 iTemp0
    ENDIF

    GET_LABEL_POINTER Buffer iTemp0
    STRING_FORMAT iTemp0 "VWW%d" weaponID
    GET_TEXT_LABEL_STRING $iTemp0 iTemp0
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

NotFindPlayerPed:
IF GOSUB FindPlayerPed
    RETURN_FALSE
ELSE
    RETURN_TRUE
ENDIF

RETURN

IsWeaponValid:
IF weaponID = 0
    IF iTemp0 = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
ELSE
    IF iTemp0 = 9
        IF totalAmmo = 0
            RETURN_FALSE
        ELSE
            RETURN_TRUE
        ENDIF
    ELSE
        READ_STRUCT_OFFSET settings 124 4 iTemp0
        READ_STRUCT_OFFSET iTemp0 4 4 iTemp0

        IF totalAmmo = 0
        AND iTemp0 = FALSE //KeepNoAmmo
            RETURN_FALSE
        ELSE
            RETURN_TRUE
        ENDIF
    ENDIF
ENDIF

RETURN

TranslateSlot0:
READ_STRUCT_OFFSET settings 12 4 iTemp0
weaponSlot = selectedUISlot - 1
weaponSlot *= 4
READ_STRUCT_OFFSET iTemp0 weaponSlot 4 weaponSlot

RETURN

TranslateSlot1:
READ_STRUCT_OFFSET settings 12 4 iTemp1
iTemp0 *= 4
READ_STRUCT_OFFSET iTemp1 iTemp0 4 iTemp0

RETURN

DeTranslateSlot:
READ_STRUCT_OFFSET settings 12 4 iTemp1

REPEAT 12 iTemp3
    iTemp2 = iTemp3
    iTemp2 *= 4
    READ_STRUCT_OFFSET iTemp1 iTemp2 4 iTemp2

    IF iTemp0 = iTemp2
        selectedUISlot = iTemp3
        selectedUISlot ++
    ENDIF
ENDREPEAT

RETURN

SCRIPT_END
}

Buffer:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //32 bytes
ENDDUMP