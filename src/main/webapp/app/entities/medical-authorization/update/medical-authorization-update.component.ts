import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClinicHistory } from 'app/entities/clinic-history/clinic-history.model';
import { ClinicHistoryService } from 'app/entities/clinic-history/service/clinic-history.service';
import { State } from 'app/entities/enumerations/state.model';
import { MedicalAuthorizationService } from '../service/medical-authorization.service';
import { IMedicalAuthorization } from '../medical-authorization.model';
import { MedicalAuthorizationFormService, MedicalAuthorizationFormGroup } from './medical-authorization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medical-authorization-update',
  templateUrl: './medical-authorization-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalAuthorizationUpdateComponent implements OnInit {
  isSaving = false;
  medicalAuthorization: IMedicalAuthorization | null = null;
  stateValues = Object.keys(State);

  clinicHistoriesSharedCollection: IClinicHistory[] = [];

  protected medicalAuthorizationService = inject(MedicalAuthorizationService);
  protected medicalAuthorizationFormService = inject(MedicalAuthorizationFormService);
  protected clinicHistoryService = inject(ClinicHistoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalAuthorizationFormGroup = this.medicalAuthorizationFormService.createMedicalAuthorizationFormGroup();

  compareClinicHistory = (o1: IClinicHistory | null, o2: IClinicHistory | null): boolean =>
    this.clinicHistoryService.compareClinicHistory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalAuthorization }) => {
      this.medicalAuthorization = medicalAuthorization;
      if (medicalAuthorization) {
        this.updateForm(medicalAuthorization);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalAuthorization = this.medicalAuthorizationFormService.getMedicalAuthorization(this.editForm);
    if (medicalAuthorization.id !== null) {
      this.subscribeToSaveResponse(this.medicalAuthorizationService.update(medicalAuthorization));
    } else {
      this.subscribeToSaveResponse(this.medicalAuthorizationService.create(medicalAuthorization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalAuthorization>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicalAuthorization: IMedicalAuthorization): void {
    this.medicalAuthorization = medicalAuthorization;
    this.medicalAuthorizationFormService.resetForm(this.editForm, medicalAuthorization);

    this.clinicHistoriesSharedCollection = this.clinicHistoryService.addClinicHistoryToCollectionIfMissing<IClinicHistory>(
      this.clinicHistoriesSharedCollection,
      medicalAuthorization.clinicHistory,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clinicHistoryService
      .query()
      .pipe(map((res: HttpResponse<IClinicHistory[]>) => res.body ?? []))
      .pipe(
        map((clinicHistories: IClinicHistory[]) =>
          this.clinicHistoryService.addClinicHistoryToCollectionIfMissing<IClinicHistory>(
            clinicHistories,
            this.medicalAuthorization?.clinicHistory,
          ),
        ),
      )
      .subscribe((clinicHistories: IClinicHistory[]) => (this.clinicHistoriesSharedCollection = clinicHistories));
  }
}
