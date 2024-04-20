import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { State } from 'app/entities/enumerations/state.model';
import { ITypeSpecialist } from '../type-specialist.model';
import { TypeSpecialistService } from '../service/type-specialist.service';
import { TypeSpecialistFormService, TypeSpecialistFormGroup } from './type-specialist-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-specialist-update',
  templateUrl: './type-specialist-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeSpecialistUpdateComponent implements OnInit {
  isSaving = false;
  typeSpecialist: ITypeSpecialist | null = null;
  stateValues = Object.keys(State);

  protected typeSpecialistService = inject(TypeSpecialistService);
  protected typeSpecialistFormService = inject(TypeSpecialistFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeSpecialistFormGroup = this.typeSpecialistFormService.createTypeSpecialistFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSpecialist }) => {
      this.typeSpecialist = typeSpecialist;
      if (typeSpecialist) {
        this.updateForm(typeSpecialist);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeSpecialist = this.typeSpecialistFormService.getTypeSpecialist(this.editForm);
    if (typeSpecialist.id !== null) {
      this.subscribeToSaveResponse(this.typeSpecialistService.update(typeSpecialist));
    } else {
      this.subscribeToSaveResponse(this.typeSpecialistService.create(typeSpecialist));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeSpecialist>>): void {
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

  protected updateForm(typeSpecialist: ITypeSpecialist): void {
    this.typeSpecialist = typeSpecialist;
    this.typeSpecialistFormService.resetForm(this.editForm, typeSpecialist);
  }
}
