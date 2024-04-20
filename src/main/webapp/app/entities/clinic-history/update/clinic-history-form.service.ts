import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClinicHistory, NewClinicHistory } from '../clinic-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClinicHistory for edit and NewClinicHistoryFormGroupInput for create.
 */
type ClinicHistoryFormGroupInput = IClinicHistory | PartialWithRequiredKeyOf<NewClinicHistory>;

type ClinicHistoryFormDefaults = Pick<NewClinicHistory, 'id'>;

type ClinicHistoryFormGroupContent = {
  id: FormControl<IClinicHistory['id'] | NewClinicHistory['id']>;
  dateClinic: FormControl<IClinicHistory['dateClinic']>;
  typeDisease: FormControl<IClinicHistory['typeDisease']>;
  people: FormControl<IClinicHistory['people']>;
};

export type ClinicHistoryFormGroup = FormGroup<ClinicHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClinicHistoryFormService {
  createClinicHistoryFormGroup(clinicHistory: ClinicHistoryFormGroupInput = { id: null }): ClinicHistoryFormGroup {
    const clinicHistoryRawValue = {
      ...this.getFormDefaults(),
      ...clinicHistory,
    };
    return new FormGroup<ClinicHistoryFormGroupContent>({
      id: new FormControl(
        { value: clinicHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateClinic: new FormControl(clinicHistoryRawValue.dateClinic, {
        validators: [Validators.required],
      }),
      typeDisease: new FormControl(clinicHistoryRawValue.typeDisease, {
        validators: [Validators.required],
      }),
      people: new FormControl(clinicHistoryRawValue.people, {
        validators: [Validators.required],
      }),
    });
  }

  getClinicHistory(form: ClinicHistoryFormGroup): IClinicHistory | NewClinicHistory {
    return form.getRawValue() as IClinicHistory | NewClinicHistory;
  }

  resetForm(form: ClinicHistoryFormGroup, clinicHistory: ClinicHistoryFormGroupInput): void {
    const clinicHistoryRawValue = { ...this.getFormDefaults(), ...clinicHistory };
    form.reset(
      {
        ...clinicHistoryRawValue,
        id: { value: clinicHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClinicHistoryFormDefaults {
    return {
      id: null,
    };
  }
}
