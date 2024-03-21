<!--<template>-->

<!--  <v-dialog v-model="dialogVisible" max-width="500px">-->

<!--    <v-card>-->
<!--      <v-card-title>-->
<!--        <span class="text-h5">{{ formTitle }}</span>-->
<!--      </v-card-title>-->

<!--      <v-card-text>-->
<!--        <v-container>-->
<!--          <v-row>-->

<!--            <v-col>-->
<!--              <v-text-field-->
<!--                  append-outer-icon="mdi-reload"-->
<!--                  @click:append-outer="generateStartNumber()"-->
<!--                  type="number"-->
<!--                  v-model="editedItem.startNumber"-->
<!--                  label="Startnummer">-->

<!--              </v-text-field>-->
<!--            </v-col>-->

<!--          </v-row>-->
<!--          <v-row>-->
<!--            <v-col>-->
<!--              <v-text-field v-model="editItem.firstName" label="Vorname"></v-text-field>-->
<!--            </v-col>-->
<!--          </v-row>-->
<!--          <v-row>-->
<!--            <v-col>-->
<!--              <v-text-field v-model="editItem.lastName" label="Nachname"></v-text-field>-->
<!--            </v-col>-->
<!--          </v-row>-->
<!--          <v-row>-->
<!--            <v-col>-->
<!--              <v-text-field v-model="editItem.city" label="Stadt"></v-text-field>-->
<!--            </v-col>-->
<!--          </v-row>-->
<!--          <v-row>-->
<!--            <v-col>-->
<!--              <v-text-field v-model="editItem.club" label="Verein"></v-text-field>-->
<!--            </v-col>-->
<!--          </v-row>-->
<!--          <v-row>-->
<!--            <v-col>-->

<!--              <v-dialog-->
<!--                  ref="dialog"-->
<!--                  v-model="modal"-->
<!--                  :return-value.sync="editItem.dateOfBirth"-->
<!--                  persistent-->
<!--                  width="290px"-->
<!--              >-->
<!--                <template v-slot:activator="{ on, attrs }">-->
<!--                  <v-text-field-->
<!--                      v-model="editItem.dateOfBirth"-->
<!--                      label="Geburtsdatum"-->
<!--                      append-outer-icon="mdi-calendar"-->
<!--                      readonly-->
<!--                      v-bind="attrs"-->
<!--                      v-on="on"-->
<!--                  ></v-text-field>-->
<!--                </template>-->
<!--                <v-date-picker-->
<!--                    v-model="editItem.dateOfBirth"-->
<!--                    scrollable-->
<!--                >-->
<!--                  <v-spacer></v-spacer>-->
<!--                  <v-btn-->
<!--                      text-->
<!--                      color="primary"-->
<!--                      @click="modal = false"-->
<!--                  >-->
<!--                    Abbrechen-->
<!--                  </v-btn>-->
<!--                  <v-btn-->
<!--                      text-->
<!--                      color="primary"-->
<!--                      @click="$refs.dialog.save(editItem.dateOfBirth)"-->
<!--                  >-->
<!--                    OK-->
<!--                  </v-btn>-->
<!--                </v-date-picker>-->
<!--              </v-dialog>-->

<!--            </v-col>-->
<!--          </v-row>-->

<!--          <v-row>-->
<!--            <v-col>-->

<!--              <v-select-->
<!--                  v-model="editItem.gender"-->
<!--                  :items="['Herren', 'Damen']"-->
<!--                  label="Geschlecht"/>-->

<!--            </v-col>-->

<!--          </v-row>-->

<!--        </v-container>-->
<!--      </v-card-text>-->

<!--      <v-card-actions>-->
<!--        <v-spacer></v-spacer>-->
<!--        <v-btn color="blue darken-1" text @click="close">Abbrechen</v-btn>-->
<!--        <v-btn color="blue darken-1" text @click="save">Speichern</v-btn>-->
<!--      </v-card-actions>-->

<!--    </v-card>-->
<!--  </v-dialog>-->

<!--</template>-->
<!--<script>-->

<!--import axios from "axios";-->
<!--import {Properties} from "@/config";-->

<!--export default {-->

<!--  props: {-->
<!--    dialog: Boolean,-->
<!--    itemprop: Object,-->
<!--  },-->

<!--  data() {-->
<!--    return {}-->
<!--  },-->

<!--  created() {-->
<!--    this.initialize();-->
<!--  },-->

<!--  mounted() {-->
<!--    this.initialize();-->
<!--  },-->

<!--  methods: {-->

<!--    initialize() {-->

<!--      this.generateStartNumber()-->

<!--    },-->

<!--    generateStartNumber() {-->
<!--      axios-->
<!--          .get(Properties.API_IP + '/competitor/generateStartNumber')-->
<!--          .then(response => {-->
<!--            console.log(response.data);-->
<!--            this.editItem.startNumber = response.data;-->
<!--          })-->
<!--          .catch(error => {-->
<!--            console.log(error);-->
<!--          });-->
<!--    },-->

<!--    close(cf) {-->
<!--      this.dialogVisible = false-->
<!--      this.$emit('dialog-closed', cf)-->
<!--    },-->


<!--    save() {-->

<!--      if (this.editItem > -1) {-->

<!--        // Object.assign(this.competitors[this.editedIndex], this.editedItem)-->

<!--        axios-->
<!--            .post(Properties.API_IP + '/competitor/update', this.editItem)-->
<!--            .then(data => {-->
<!--              console.log(data);-->
<!--            })-->
<!--            .catch(error => {-->
<!--              console.log(error);-->
<!--            }).finally(() => {-->
<!--          this.close();-->
<!--        });-->

<!--        this.close()-->

<!--      } else {-->

<!--        // New competitor-->

<!--        axios-->
<!--            .post(Properties.API_IP + '/competitor/create', this.editItem)-->
<!--            .then(data => {-->
<!--              console.log(data);-->
<!--            })-->
<!--            .catch(error => {-->
<!--              console.log(error);-->
<!--            }).finally(() => {-->
<!--          this.close();-->
<!--        });-->

<!--      }-->


<!--    },-->

<!--  },-->

<!--  computed: {-->

<!--    formTitle() {-->
<!--      return 'Erstellen'-->
<!--    },-->

<!--    // formSubtitle() {-->
<!--    //   return 'Function: ' +  this.editItem.functionName-->
<!--    // },-->

<!--    dialogVisible: {-->
<!--      get() {-->
<!--        return this.dialog-->
<!--      },-->
<!--      set(value) {-->
<!--        this.$emit('update:dialog', value)-->
<!--      }-->
<!--    },-->

<!--    editItem: {-->
<!--      get() {-->
<!--        return this.itemprop ? this.itemprop : new Object()-->
<!--      },-->
<!--      set(value) {-->
<!--        this.$emit('update:itemProp', value)-->
<!--      }-->
<!--    }-->

<!--  },-->

<!--}-->

<!--</script>-->