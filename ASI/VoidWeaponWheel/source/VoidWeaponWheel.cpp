#include <plugin.h> // Plugin-SDK version 1002 from 2025-12-09 23:18:09
#include <CMessages.h>
#include <CTxdStore.h>
#include "../third_party/IniReader/IniReader.h"

#define GINPUT_COMPILE_SA_VERSION
#include "../third_party/GInputAPI/GInputAPI.h"

using namespace plugin;

//char msg[255];

bool gInputFound;

IGInputPad* pads[2];

GINPUT_GENERAL_SETTINGS gSettings;

uintptr_t GetWeaponJumpAddr;
uintptr_t GetWeaponHookAddr;
uintptr_t WeaponJustDownJumpAddr;
uintptr_t WeaponJustDownHookAddr;

using GenericFn = int(__thiscall*)(CPad*, CPed*);

int weaponWheelState[2];
bool cameraControlState[2] = {true, true};
bool canAttackState[2] = {true, true};

extern "C"
{
    __declspec(dllexport) int __cdecl GetMapPadOneToPadTwo() //To be used by VWW only
    {
        return gSettings.MapPadOneToPadTwo;
    }

    __declspec(dllexport) int __cdecl GetHasPadInHands(int pad) //To be used by VWW only
    {
        return pads[pad]->HasPadInHands();
    }

    __declspec(dllexport) void __cdecl SetWeaponWheelState(int player, int state) //To be used by VWW only
    {
        weaponWheelState[player] = state;
    }

    __declspec(dllexport) int __cdecl GetWeaponWheelState(int player) //0: Closed / 1: Opened / 2: Opening(delay before open)
    {
        return weaponWheelState[player];
    }

    __declspec(dllexport) void __cdecl SetCameraControlState(int player, int state) //To be used by VWW only
    {
        cameraControlState[player] = state;
    }

    __declspec(dllexport) int __cdecl GetCameraControlState(int player)
    {
        return cameraControlState[player];
    }

    __declspec(dllexport) void __cdecl SetCanAttackState(int player, int state) //To be used by VWW only
    {
        canAttackState[player] = state;
    }

    __declspec(dllexport) int __cdecl GetCanAttackState(int player)
    {
        return canAttackState[player];
    }
}

bool CheckCanAttackState(CPad* pad)
{
    int playerID = (uintptr_t)(pad) != 0xB73458;

    return canAttackState[playerID];
}

int GetWeaponOG(CPad* _this, CPed* ped)
{
    if(_this->DisablePlayerControls || _this->bDisablePlayerFireWeapon)
        return 0;

    switch(_this->Mode)
    {
    case 0:
    case 1:
        if(ped && !_this->bDisablePlayerFireWeaponWithL1)
        {
            CTask* task = ped->m_pIntelligence->m_TaskMgr.GetSimplestActiveTask();
            if(ped->m_pIntelligence->GetTaskUseGun() || (task && task->GetId() == TASK_SIMPLE_GANG_DRIVEBY) || ped->m_pAttachedTo)
            {
                return _this->NewState.ButtonCircle + _this->NewState.LeftShoulder1;
            }
        }
        return _this->NewState.ButtonCircle;
    case 2:
        return _this->NewState.ButtonCross;
    case 3:
        return _this->NewState.RightShoulder1;
    }

    return 0;
}

int __fastcall GetWeaponJump(CPad* _this, void* edx, CPed* ped)
{
    if(!CheckCanAttackState(_this))
        return 0;

    if(gInputFound)
        return reinterpret_cast<GenericFn>(GetWeaponJumpAddr)(_this, ped);

    return GetWeaponOG(_this, ped);
}

int __fastcall GetWeaponHook(CPad* _this, void* edx, CPed* ped)
{
    if(!CheckCanAttackState(_this))
        return 0;

    if(gInputFound)
        return reinterpret_cast<GenericFn>(GetWeaponHookAddr)(_this, ped);

    return GetWeaponOG(_this, ped);
}

int WeaponJustDownOG(CPad* _this, CPed* ped)
{
    if(_this->DisablePlayerControls || _this->bDisablePlayerDisplayVitalStats)
        return 0;

    switch(_this->Mode)
    {
    case 0:
    case 1:
        if(_this->NewState.ButtonCircle && !_this->OldState.ButtonCircle)
            return 1;

        if(ped && !_this->bDisablePlayerFireWeaponWithL1)
        {
            CTask* task = ped->m_pIntelligence->m_TaskMgr.GetSimplestActiveTask();
            if(ped->m_pIntelligence->GetTaskUseGun() || (task && task->GetId() == TASK_SIMPLE_GANG_DRIVEBY) || ped->m_pAttachedTo)
            {
                return _this->NewState.LeftShoulder1 && !_this->OldState.LeftShoulder1;
            }
        }
        return 0;
    case 2:
        return _this->NewState.ButtonCross && !_this->OldState.ButtonCross;
    case 3:
        return _this->NewState.RightShoulder1 && !_this->OldState.RightShoulder1;
    }

    return 0;
}

int __fastcall WeaponJustDownJump(CPad* _this, void* edx, CPed* ped)
{
    if(!CheckCanAttackState(_this))
        return 0;

    if(gInputFound)
        return reinterpret_cast<GenericFn>(WeaponJustDownJumpAddr)(_this, ped);

    return WeaponJustDownOG(_this, ped);
}

int __fastcall WeaponJustDownHook(CPad* _this, void* edx, CPed* ped)
{
    if(!CheckCanAttackState(_this))
        return 0;

    if(gInputFound)
        return reinterpret_cast<GenericFn>(WeaponJustDownHookAddr)(_this, ped);

    return WeaponJustDownOG(_this, ped);
}

int __fastcall GetMeleeAttackJump(CPad* _this, void* edx, bool checkAllButtons)
{
    if(!CheckCanAttackState(_this))
        return 0;

    if(_this->DisablePlayerControls)
        return 0;

    if(_this->NewState.ButtonCircle)
        return 1;

    if(checkAllButtons)
    {
        if(_this->NewState.ButtonCross)
            return 2;

        if(_this->NewState.ButtonSquare)
            return 3;

        if(_this->NewState.ButtonTriangle)
            return 4;
    }

    return 0;
}

int __fastcall MeleeAttackJustDownJump(CPad* _this, void* edx, bool checkAllButtons)
{
    if(!CheckCanAttackState(_this))
        return 0;

    if(_this->DisablePlayerControls)
        return 0;

    if(_this->NewState.ButtonCircle && !_this->OldState.ButtonCircle)
        return 1;

    if(checkAllButtons)
    {
        if(_this->NewState.ButtonCross && !_this->OldState.ButtonCross)
            return 2;

        if(_this->NewState.ButtonSquare)
            return 3;

        if(_this->NewState.ButtonTriangle && !_this->OldState.ButtonTriangle)
            return 4;
    }

    return 0;
}

void ModProcess()
{
    pads[0]->SendConstEvent(GINPUT_EVENT_FETCH_GENERAL_SETTINGS, &gSettings);
}

struct Main
{
    Main()
    {
        CIniReader ini("VoidWeaponWheel.ini");
        bool disableOnSAMP = ini.ReadInteger("configs", "DisableOnSAMP", 0);

        if(GetModuleHandleA("SAMP.dll") && disableOnSAMP) return;

        Events::gameProcessEvent += ModProcess;

        Events::initRwEvent.after += []
        {
            GInput_Load_TwoPads(pads); //Yeah this can be done without checking for GInput first
            gSettings.cbSize = sizeof(gSettings);
            pads[0]->SendConstEvent(GINPUT_EVENT_FETCH_GENERAL_SETTINGS, &gSettings);

            if(GetModuleHandleA("GInputSA.asi"))
            {
                gInputFound = true;

                int offset;
                offset = *(int*)(0x540180 + 1);
                GetWeaponJumpAddr = 0x540180 + 5 + offset; //Saves GInput jump address to be called later. Original function GetWeapon

                offset = *(int*)(0x68628E + 1);
                GetWeaponHookAddr = 0x68628E + 5 + offset; //Saves GInput hook address to be called later. Original call GetWeapon

                offset = *(int*)(0x540250 + 1);
                WeaponJustDownJumpAddr = 0x540250 + 5 + offset; //Saves GInput jump address to be called later. Original function WeaponJustDown

                offset = *(int*)(0x688B2F + 1);
                WeaponJustDownHookAddr = 0x688B2F + 5 + offset; //Saves GInput hook address to be called later. Original call WeaponJustDown

                patch::RedirectCall(0x68628E, GetWeaponHook); //Redirects to my function instead of GInput function
                patch::RedirectCall(0x686FC2, GetWeaponHook); //Redirects to my function instead of GInput function

                patch::RedirectCall(0x688B2F, WeaponJustDownHook); //Redirects to my function instead of GInput function
            }
            
            patch::ReplaceFunction(0x540180, GetWeaponJump); //Replaces GInput jump if it exists
            patch::ReplaceFunction(0x540250, WeaponJustDownJump); //Replaces GInput jump if it exists
            patch::ReplaceFunction(0x540340, GetMeleeAttackJump);
            patch::ReplaceFunction(0x540390, MeleeAttackJustDownJump);

            int slot = CTxdStore::AddTxdSlot("weapvww");
            CTxdStore::LoadTxd(slot, "models\\txd\\weapvww.txd");
            CTxdStore::AddRef(slot);
        };
    }
} gInstance;