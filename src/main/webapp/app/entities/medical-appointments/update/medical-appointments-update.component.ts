import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeSpecialist } from 'app/entities/type-specialist/type-specialist.model';
import { TypeSpecialistService } from 'app/entities/type-specialist/service/type-specialist.service';
import { IMedicalAppointments } from '../medical-appointments.model';
import { MedicalAppointmentsService } from '../service/medical-appointments.service';
import { MedicalAppointmentsFormService, MedicalAppointmentsFormGroup } from './medical-appointments-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medical-appointments-update',
  templateUrl: './medical-appointments-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalAppointmentsUpdateComponent implements OnInit {
  isSaving = false;
  medicalAppointments: IMedicalAppointments | null = null;

  typeSpecialistsSharedCollection: ITypeSpecialist[] = [];

  protected medicalAppointmentsService = inject(MedicalAppointmentsService);
  protected medicalAppointmentsFormService = inject(MedicalAppointmentsFormService);
  protected typeSpecialistService = inject(TypeSpecialistService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalAppointmentsFormGroup = this.medicalAppointmentsFormService.createMedicalAppointmentsFormGroup();

  compareTypeSpecialist = (o1: ITypeSpecialist | null, o2: ITypeSpecialist | null): boolean =>
    this.typeSpecialistService.compareTypeSpecialist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalAppointments }) => {
      this.medicalAppointments = medicalAppointments;
      if (medicalAppointments) {
        this.updateForm(medicalAppointments);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalAppointments = this.medicalAppointmentsFormService.getMedicalAppointments(this.editForm);
    if (medicalAppointments.id !== null) {
      this.subscribeToSaveResponse(this.medicalAppointmentsService.update(medicalAppointments));
    } else {
      this.subscribeToSaveResponse(this.medicalAppointmentsService.create(medicalAppointments));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalAppointments>>): void {
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

  protected updateForm(medicalAppointments: IMedicalAppointments): void {
    this.medicalAppointments = medicalAppointments;
    this.medicalAppointmentsFormService.resetForm(this.editForm, medicalAppointments);

    this.typeSpecialistsSharedCollection = this.typeSpecialistService.addTypeSpecialistToCollectionIfMissing<ITypeSpecialist>(
      this.typeSpecialistsSharedCollection,
      medicalAppointments.typeSpecialist,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeSpecialistService
      .query()
      .pipe(map((res: HttpResponse<ITypeSpecialist[]>) => res.body ?? []))
      .pipe(
        map((typeSpecialists: ITypeSpecialist[]) =>
          this.typeSpecialistService.addTypeSpecialistToCollectionIfMissing<ITypeSpecialist>(
            typeSpecialists,
            this.medicalAppointments?.typeSpecialist,
          ),
        ),
      )
      .subscribe((typeSpecialists: ITypeSpecialist[]) => (this.typeSpecialistsSharedCollection = typeSpecialists));
  }
}
